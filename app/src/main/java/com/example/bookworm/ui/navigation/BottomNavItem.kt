package com.example.bookworm.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home: BottomNavItem(
        route = NavRoutes.Home.route,
        label = "home",
        icon = Icons.Default.Home
    )
    object Library: BottomNavItem(
        route = NavRoutes.Library.route,
        label = "Library",
        icon = Icons.Default.MenuBook
    )
    object Search: BottomNavItem(
        route = NavRoutes.Search.route,
        label = "Search",
        icon = Icons.Default.Search
    )
    object Stats: BottomNavItem(
        route = NavRoutes.Stats.route,
        label = "Stats",
        icon = Icons.Default.BarChart
    )
}