package com.app.task_itzon.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.task_itzon.data.local.entity.FavoriteProductEntity
import com.app.task_itzon.data.model.Product
import com.app.task_itzon.data.model.Products
import com.app.task_itzon.data.repo.ProductsRepository
import com.app.task_itzon.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: ProductsRepository) : ViewModel() {

    private val _products = MutableStateFlow<Resource<Products>>(Resource.Loading())
    val products: StateFlow<Resource<Products>> = _products.asStateFlow()

    private val _product = MutableStateFlow<Resource<Product>>(Resource.Loading())
    val product: StateFlow<Resource<Product>> = _product.asStateFlow()

    val favorites: StateFlow<List<FavoriteProductEntity>> = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getProducts() {
        if (_products.value is Resource.Success) return
        _products.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProducts().collect {
                _products.value = it
            }
        }
    }

    fun getProduct(id: Int) {
        val current = _product.value
        if (current is Resource.Success && current.data?.id == id) return

        _product.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProduct(id).collect {
                _product.value = it
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFav = favorites.value.any { it.id == product.id }
            if (isFav) {
                repository.deleteFavorite(product.toEntity())
            } else {
                repository.insertFavorite(product.toEntity())
            }
        }
    }

    fun isFavorite(productId: Int): Flow<Boolean> = repository.isFavorite(productId)

    private fun Product.toEntity() = FavoriteProductEntity(
        id = id,
        title = title,
        brand = brand,
        price = price,
        thumbnail = thumbnail,
        category = category
    )
}
