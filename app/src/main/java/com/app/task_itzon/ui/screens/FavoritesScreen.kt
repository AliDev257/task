package com.app.task_itzon.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.task_itzon.data.local.entity.FavoriteProductEntity
import com.app.task_itzon.data.model.Product
import com.app.task_itzon.ui.viewmodel.ProductsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onProductClick: (Product) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ProductsViewModel = koinViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (favorites.isEmpty()) {
                Text(
                    text = "No favorites yet",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(favorites) { favorite ->
                        val product = favorite.toProduct()
                        ProductItem(
                            product = product,
                            isFavorite = true,
                            onFavoriteClick = { viewModel.toggleFavorite(product) },
                            onClick = { onProductClick(product) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

private fun FavoriteProductEntity.toProduct() = Product(
    id = id,
    title = title,
    brand = brand,
    price = price,
    thumbnail = thumbnail,
    category = category
)
