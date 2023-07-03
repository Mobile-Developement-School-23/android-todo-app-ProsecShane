package com.prosecshane.todoapp.data.database.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.prosecshane.todoapp.data.Constants.HTTP_REQUEST_VALUE
import com.prosecshane.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class NetCallback(private val scope: CoroutineScope) : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        TodoItemsRepository.connected.set(isOnline())
        if (TodoItemsRepository.sharedPreferences.contains(HTTP_REQUEST_VALUE)) {
            scope.launch { TodoItemsRepository.sync() }
        }
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        TodoItemsRepository.connected.set(isOnline())
    }

    private fun isOnline(): Boolean {
        val capabilities = TodoItemsRepository.connectivityManager
            .getNetworkCapabilities(TodoItemsRepository.connectivityManager.activeNetwork)
        if (capabilities != null) {
            return (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        }
        return false
    }
}
