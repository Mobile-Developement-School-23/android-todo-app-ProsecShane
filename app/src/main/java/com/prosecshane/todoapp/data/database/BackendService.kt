package com.prosecshane.todoapp.data.database

import com.prosecshane.todoapp.data.database.response.ItemListResponse
import com.prosecshane.todoapp.data.database.response.ItemResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BackendService {
    @GET("list")
    suspend fun getItemsList(): Response<ItemListResponse>

    @PATCH("list")
    suspend fun patchItemList(@Body list: ItemListResponse): Response<ItemListResponse>

    @GET("list/{id}")
    suspend fun getItem(@Path("id") id: String): Response<ItemResponse>

    @PUT("list/{id}")
    suspend fun patchItem(@Path("id") id: String, @Body item: ItemResponse): Response<ItemResponse>

    @POST("list")
    suspend fun addItem(@Body item: ItemResponse): Response<ItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteItem(@Path("id") id: String): Response<ItemResponse>
}
