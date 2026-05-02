package com.app.task_itzon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.task_itzon.data.model.Product
import com.app.task_itzon.ui.viewmodel.ProductsViewModel
import com.app.task_itzon.ui.viewmodel.SearchViewModel
import com.app.task_itzon.util.Resource
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductScreen(
    onProductClick: (Product) -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: ProductsViewModel = koinViewModel(),
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val productsState by viewModel.products.collectAsState()
    val searchQuery by searchViewModel.searchQuery.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchViewModel.onSearchQueryChange(it) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search by name, brand, category, SKU") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )
            IconButton(onClick = onFavoritesClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Box(modifier = Modifier.weight(1f).align(Alignment.CenterHorizontally)) {
            when (val state = productsState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Success -> {
                    val products = state.data?.products ?: emptyList()
                    val filteredProducts = searchViewModel.filterProducts(products, searchQuery)
                    
                    if (filteredProducts.isEmpty()) {
                        Text(
                            text = "No products found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(filteredProducts) { product ->
                                val isFavorite = favorites.any { it.id == product.id }
                                ProductItem(
                                    product = product,
                                    isFavorite = isFavorite,
                                    onFavoriteClick = { viewModel.toggleFavorite(product) },
                                    onClick = { onProductClick(product) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = state.message ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.thumbnail,
            contentDescription = product.title,
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = product.brand, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
