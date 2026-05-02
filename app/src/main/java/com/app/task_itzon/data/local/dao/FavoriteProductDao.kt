package com.app.task_itzon.data.local.dao

import androidx.room.*
import com.app.task_itzon.data.local.entity.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(product: FavoriteProductEntity)

    @Delete
    suspend fun deleteFavorite(product: FavoriteProductEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :productId)")
    fun isFavorite(productId: Int): Flow<Boolean>
}
