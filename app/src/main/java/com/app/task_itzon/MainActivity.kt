package com.app.task_itzon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.app.task_itzon.navigation.Favorites
import com.app.task_itzon.navigation.Navigator
import com.app.task_itzon.navigation.ProductDetails
import com.app.task_itzon.navigation.ProductList
import com.app.task_itzon.navigation.rememberNavigationState
import com.app.task_itzon.navigation.toEntries
import com.app.task_itzon.ui.screens.FavoritesScreen
import com.app.task_itzon.ui.screens.ProductDetailsScreen
import com.app.task_itzon.ui.screens.ProductScreen
import com.app.task_itzon.ui.theme.Task_ItzonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task_ItzonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.fillMaxSize().padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navigationState = rememberNavigationState(
        startRoute = ProductList,
        topLevelRoutes = setOf(ProductList)
    )
    val navigator = Navigator(navigationState)

    val entries = navigationState.toEntries { key ->
        when (key) {
            is ProductList -> NavEntry<NavKey>(key) {
                ProductScreen(
                    onProductClick = { product -> navigator.navigate(ProductDetails(product.id)) },
                    onFavoritesClick = { navigator.navigate(Favorites) }
                )
            }
            is Favorites -> NavEntry<NavKey>(key) {
                FavoritesScreen(
                    onProductClick = { product -> navigator.navigate(ProductDetails(product.id)) },
                    onBackClick = { navigator.goBack() }
                )
            }
            is ProductDetails -> NavEntry<NavKey>(key) { ProductDetailsScreen(productId = key.id) }
            else -> error("Unknown key: $key")
        }
    }

    NavDisplay(
        entries = entries,
        onBack = { navigator.goBack() },
        modifier = modifier
    )
}