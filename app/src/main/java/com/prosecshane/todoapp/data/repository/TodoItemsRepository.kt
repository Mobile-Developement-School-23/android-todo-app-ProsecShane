package com.prosecshane.todoapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.provider.Settings
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prosecshane.todoapp.data.Constants.DEVICE_UID
import com.prosecshane.todoapp.data.Constants.HTTP_REQUEST_VALUE
import com.prosecshane.todoapp.data.Constants.LAST_KNOWN_REVISION
import com.prosecshane.todoapp.data.Constants.SHARED_PREFERENCES
import com.prosecshane.todoapp.data.database.BackendService
import com.prosecshane.todoapp.data.database.TodoItemDatabase
import com.prosecshane.todoapp.data.database.network.RetrofitClient
import com.prosecshane.todoapp.data.database.response.ItemListResponse
import com.prosecshane.todoapp.data.database.response.ItemResponse
import com.prosecshane.todoapp.data.datasource.TodoItemsDataSource
import com.prosecshane.todoapp.data.model.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.create
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicBoolean

// Repository class to store items
@RequiresApi(Build.VERSION_CODES.M)
object TodoItemsRepository {
    lateinit var dataSource: TodoItemsDataSource
    // Live data of lists, that store the items (with public getter)
    private val _todoItems = MutableLiveData<List<TodoItem>>(emptyList())
    val todoItems: LiveData<List<TodoItem>> = _todoItems

    // Update the items (in Main Thread)
    @MainThread
    suspend fun updateTodoItems() {
        val loadedTodoItems = withContext(Dispatchers.IO) { dataSource.loadTodoItems() }
        _todoItems.value = loadedTodoItems
    }

    // Change an item (in Background)
    suspend fun changeTodoItem(changedTodoItem: TodoItem) {
        val newTodoItems = withContext(Dispatchers.Default) {
            todoItems.value.orEmpty().map { todoItem ->
                if (todoItem.id == changedTodoItem.id) changedTodoItem
                else todoItem.copy(editedOn = System.currentTimeMillis())
            }
        }
        _todoItems.value = newTodoItems
    }

    // Delete an item (in Background)
    suspend fun deleteTodoItem(todoItemId: String) {
        withContext(Dispatchers.Default) {
            _todoItems.postValue(todoItems.value.orEmpty().filter { it.id != todoItemId })
        }
    }

    // Add an item (in Background)
    suspend fun addTodoItem(newTodoItem: TodoItem) {
        withContext(Dispatchers.Default) {
            val appended = todoItems.value.orEmpty() as MutableList<TodoItem>
            appended.add(newTodoItem)
            _todoItems.postValue(appended)
        }
    }

    private lateinit var backendService: BackendService
    lateinit var database: TodoItemDatabase

    lateinit var sharedPreferences: SharedPreferences

    lateinit var connectivityManager: ConnectivityManager

    private var connection = false
    var connected = AtomicBoolean(false)

    private var checkConnectionJob: Job? = null

    private val _code = Channel<Int>()
    val code =  _code.receiveAsFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    @SuppressLint("HardwareIds")
    fun setupSyncing(context: Context) {
        backendService = RetrofitClient.getInstance(context).create()
        database = TodoItemDatabase.getDatabase(context)

        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(
            DEVICE_UID, Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        ).apply()

        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connection = sharedPreferences.contains(HTTP_REQUEST_VALUE)

        checkConnectionJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(667)
                val isConnectedValue = isConnected()
                if (isConnectedValue)
                    if (sharedPreferences.contains(HTTP_REQUEST_VALUE) && !connected.get())
                        sync()
                connected.set(isConnectedValue)
            }
        }

    }

    fun isConnected(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )?: return false
        return (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    suspend fun sync() {
        if (connection && isConnected()) {
            try {
                val response = backendService.getItemsList()
                sharedPreferences.edit()
                    .putInt(LAST_KNOWN_REVISION, response.body()!!.revision!!).apply()
                val x = synchronizeData(response.body()!!.itemList)
                for (i in x.second.first)   deleteItem(i, true)
                for (i in x.second.second)  backendService.addItem(ItemResponse(i))
                for (i in x.first.second)   database.todoItemDBMS().insertItem(i)
                for (i in x.first.first)    updateItem(i, true)
                _code.send(200)
            } catch (e: Exception) {
                _code.send(600)
            }
        } else {
            _code.send(600)
        }
    }

    suspend fun updateItem(todoItem: TodoItem, mute: Boolean = false) {
        database.todoItemDBMS().updateItem(todoItem)
        if (connection && connected.get()) {
            try {
                val response = backendService.patchItem(todoItem.id, ItemResponse(todoItem))
                handleResponse(response, mute)
            } catch (e: Exception) { _code.send(600) }
        }
    }

    suspend fun deleteItem(todoItem: TodoItem, mute: Boolean = false) {
        if (connection && !connected.get()) {
            todoItem.deleted = true
            todoItem.editedOn = System.currentTimeMillis()
            database.todoItemDBMS().updateItem(todoItem)
        } else {
            database.todoItemDBMS().deleteItem(todoItem)
        }
        if (connection && connected.get()) {
            try {
                val response = backendService.deleteItem(todoItem.id)
                handleResponse(response, mute)
            } catch (e: Exception) {
                _code.send(600)
            }
        }
    }

    private suspend fun handleResponse(response: Response<ItemResponse>, mute: Boolean = false) {
        if (!mute) _code.send(response.code())
        when (response.code()) {
            400, 404 -> sync()
            200 -> sharedPreferences.edit().putInt(LAST_KNOWN_REVISION, response.body()!!.revision!!).apply()
        }
    }

    private suspend fun synchronizeData(serverData: List<TodoItem>):
            Pair<Pair<List<TodoItem>, List<TodoItem>>, Pair<List<TodoItem>, List<TodoItem>>> {
        val resArr = arrayListOf<TodoItem>()
        val deleted = arrayListOf<TodoItem>()

        val newItemsDB = arrayListOf<TodoItem>()
        val newItemsServ = arrayListOf<TodoItem>()

        val added = mutableSetOf<String>()

        for (i in serverData) {
            try {
                val item = database.todoItemDBMS().getItemById(i.id)
                if (i.editedOn > item.editedOn) {
                    resArr.add(i)
                    added.add(i.id)
                } else if(i.editedOn < item.editedOn) {
                    if (item.deleted) deleted.add(item)
                    else resArr.add(item)
                    added.add(item.id)
                } else if (item.deleted) {
                    deleted.add(item)
                    added.add(item.id)
                } else {
                    added.add(item.id)
                }
            } catch (e: Exception) {
                newItemsServ.add(i)
                added.add(i.id)
            }
        }

        for (i in database.todoItemDBMS().getAllNoFlow()) {
            if (i.id !in added && !i.deleted) newItemsDB.add(i)
        }

        return Pair(Pair(resArr, newItemsServ), Pair(deleted, newItemsDB))
    }

    private fun getNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()
    }

    fun changeConnectionMode(dropData: Boolean = false) {
        connection = sharedPreferences.contains(HTTP_REQUEST_VALUE)
        if (dropData) {
            sharedPreferences.edit().remove(HTTP_REQUEST_VALUE).remove(LAST_KNOWN_REVISION).apply()
            scope.launch { database.todoItemDBMS().deleteTable() }
        }
    }

    private suspend fun cleanTableAndInsert(list: List<TodoItem>) {
        database.todoItemDBMS().deleteTable()
        database.todoItemDBMS().insertAll(list)
    }

    fun getTaskById(id: String): TodoItem {
        return database.todoItemDBMS().getItemById(id)
    }

    suspend fun addTask(todoItem: TodoItem, mute: Boolean = false) {
        database.todoItemDBMS().insertItem(todoItem)
        if (connection && connected.get()) {
            try {
                val response = backendService.addItem(ItemResponse(todoItem))
                handleResponse(response, mute)
            } catch (e: Exception) { _code.send(600) }
        }
    }

    suspend fun sendTasks(list: List<TodoItem>) {
        if (connection && connected.get()) {
            try {
                val response = backendService.patchItemList(ItemListResponse(list))
                _code.send(response.code())
                when (response.code()) {
                    400 -> sync()
                    200 -> sharedPreferences.edit().putInt(LAST_KNOWN_REVISION, response.body()!!.revision!!).apply()
                }
            } catch (e: Exception) { _code.send(600) }
        }
    }

    fun destroy() {
        scope.cancel()
        checkConnectionJob?.cancel()
    }
}
