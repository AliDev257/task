package com.app.task_itzon.data.repo

import com.app.task_itzon.data.local.dao.FavoriteProductDao
import com.app.task_itzon.data.local.entity.FavoriteProductEntity
import com.app.task_itzon.data.model.Product
import com.app.task_itzon.data.model.Products
import com.app.task_itzon.util.NoConnectivityException
import com.app.task_itzon.util.Resource
import com.example.foodiego.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class ProductsRepository(
    private val apiService: ApiService,
    private val favoriteProductDao: FavoriteProductDao
) {

    fun getProducts(): Flow<Resource<Products>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.products()
            emit(Resource.Success(response))
        } catch (e: NoConnectivityException) {
            emit(Resource.Error("No internet connection. Please check your network."))
        } catch (e: HttpException) {
            emit(Resource.Error("Server error: ${e.code()}. Please try again later."))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Something went wrong"))
        }
    }

    fun getProduct(id: Int): Flow<Resource<Product>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.productById(id)
            emit(Resource.Success(response))
        } catch (e: NoConnectivityException) {
            emit(Resource.Error("No internet connection. Please check your network."))
        } catch (e: HttpException) {
            emit(Resource.Error("Server error: ${e.code()}. Please try again later."))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Something went wrong"))
        }
    }



    fun getAllFavorites(): Flow<List<FavoriteProductEntity>> = favoriteProductDao.getAllFavorites()

    fun isFavorite(productId: Int): Flow<Boolean> = favoriteProductDao.isFavorite(productId)

    suspend fun insertFavorite(product: FavoriteProductEntity) {
        favoriteProductDao.insertFavorite(product)
    }

    suspend fun deleteFavorite(product: FavoriteProductEntity) {
        favoriteProductDao.deleteFavorite(product)
    }
}
