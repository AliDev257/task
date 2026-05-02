package com.example.foodiego.data.remote

import com.app.task_itzon.data.model.Product
import com.app.task_itzon.data.model.Products
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("products")
    suspend fun products(): Products

    @GET("products/{id}")
    suspend fun productById(@Path("id") id: Int): Product

}
