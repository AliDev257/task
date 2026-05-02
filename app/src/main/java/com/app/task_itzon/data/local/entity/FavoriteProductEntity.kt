package com.app.task_itzon.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val brand: String,
    val price: Double,
    val thumbnail: String,
    val category: String
)
