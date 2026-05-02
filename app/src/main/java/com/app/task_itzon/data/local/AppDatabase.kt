package com.app.task_itzon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.task_itzon.data.local.dao.FavoriteProductDao
import com.app.task_itzon.data.local.entity.FavoriteProductEntity

@Database(entities = [FavoriteProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao
}
