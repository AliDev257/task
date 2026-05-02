package com.app.task_itzon.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.app.task_itzon.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun filterProducts(products: List<Product>, query: String): List<Product> {
        if (query.isBlank()) return products
        
        return products.filter { product ->
            product.title.contains(query, ignoreCase = true) ||
            product.brand.contains(query, ignoreCase = true) ||
            product.category.contains(query, ignoreCase = true) ||
            product.sku.contains(query, ignoreCase = true)
        }
    }
}
