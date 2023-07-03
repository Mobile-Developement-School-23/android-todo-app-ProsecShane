package com.prosecshane.todoapp.data.database.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.prosecshane.todoapp.data.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class RetrofitClient {
    companion object {
        @Volatile
        private var instance: Retrofit? = null
        private const val url = Constants.BASE_URL

        fun getInstance(context: Context): Retrofit {
            return instance ?: Retrofit.Builder()
                .baseUrl(url)
                .client(OkHttpClient().newBuilder()
                    .addInterceptor(HttpInterceptor(context))
                    .build())
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
        }
    }
}