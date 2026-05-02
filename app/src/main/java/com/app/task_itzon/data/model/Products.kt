package com.app.task_itzon.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Products(
    @SerialName("limit")
    val limit: Int,
    @SerialName("products")
    val products: List<Product>,
    @SerialName("skip")
    val skip: Int,
    @SerialName("total")
    val total: Int
)