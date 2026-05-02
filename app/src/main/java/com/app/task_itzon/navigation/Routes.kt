package com.app.task_itzon.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object ProductList : NavKey

@Serializable
data object Favorites : NavKey

@Serializable
data class ProductDetails(val id: Int) : NavKey
