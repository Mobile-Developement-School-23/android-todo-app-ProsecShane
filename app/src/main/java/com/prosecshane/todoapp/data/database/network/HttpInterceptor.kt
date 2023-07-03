package com.prosecshane.todoapp.data.database.network

import android.content.Context
import com.prosecshane.todoapp.data.Constants.HTTP_REQUEST_TYPE
import com.prosecshane.todoapp.data.Constants.HTTP_REQUEST_VALUE
import com.prosecshane.todoapp.data.Constants.LAST_KNOWN_REVISION
import com.prosecshane.todoapp.data.Constants.SHARED_PREFERENCES
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HttpInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val lastKnownRevision = sharedPreferences.getInt(LAST_KNOWN_REVISION, 0)
        val newReq = request.newBuilder()
            .header("Authorization", "$HTTP_REQUEST_TYPE $HTTP_REQUEST_VALUE")
            .header("X-Last-Known-Revision", lastKnownRevision.toString()).build()
        return chain.proceed(newReq)
    }
}