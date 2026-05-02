package com.app.task_itzon.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.app.task_itzon.util.Resource
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailsScreen(
    productId: Int,
    viewModel: ProductsViewModel = koinViewModel()
) {
    val productState by viewModel.product.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    LaunchedEffect(productId) {
        viewModel.getProduct(productId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = productState) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is Resource.Success -> {
                val product = state.data
                if (product != null) {
                    val isFavorite = favorites.any { it.id == product.id }
                    ProductDetailsContent(
                        product = product,
                        isFavorite = isFavorite,
                        onFavoriteClick = { viewModel.toggleFavorite(product) }
                    )
                } else {
                    Text(
                        text = "Product not found",
                        modifier = Modifier.align(Alignment.Center)
                    )
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

@Composable
fun ProductDetailsContent(
    product: Product,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(product.images) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = product.title,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Price: $${product.price}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                if (product.discountPercentage > 0) {
                    Text(
                        text = "Discount: ${product.discountPercentage}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        item {
            Text(
                text = "Brand: ${product.brand}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Text(
                text = "Category: ${product.category}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Text(
                text = "Rating: ${product.rating}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Text(
                text = "Stock: ${product.stock}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Text(
                text = "Availability: ${product.availabilityStatus}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            HorizontalDivider()
        }

        item {
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            HorizontalDivider()
        }

        item {
            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("SKU: ${product.sku}")
                Text("Warranty: ${product.warrantyInformation}")
                Text("Shipping: ${product.shippingInformation}")
                Text("Return Policy: ${product.returnPolicy}")
                Text("Minimum Order Quantity: ${product.minimumOrderQuantity}")
                Text("Tags: ${product.tags.joinToString(", ")}")
            }
        }

        if (product.reviews.isNotEmpty()) {
            item {
                HorizontalDivider()
            }

            item {
                Text(
                    text = "Reviews",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(product.reviews) { review ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = review.reviewerName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${review.rating}/5",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = review.comment,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = review.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                HorizontalDivider()
            }
        }
    }
}