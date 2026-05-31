package com.example.bookworm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookworm.ui.detail.BookDetailScreen
import com.example.bookworm.ui.home.HomeScreen
import com.example.bookworm.ui.library.LibraryScreen
import com.example.bookworm.ui.search.SearchScreen
import com.example.bookworm.ui.stats.StatsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
        modifier = modifier
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                onBookClick = { bookId ->
                    navController.navigate(NavRoutes.BookDetail.createRoute(bookId))
                },
                onSeeAllClick = {
                    navController.navigate(NavRoutes.Library.route)
                }
            )
        }

        composable(NavRoutes.Library.route) {
            LibraryScreen(
                onBookClick = { bookId ->
                    navController.navigate(NavRoutes.BookDetail.createRoute(bookId))
                }
            )
        }

        composable(NavRoutes.Search.route) {
            SearchScreen(
                onBookClick = { bookId ->
                    navController.navigate(NavRoutes.BookDetail.createRoute(bookId))
                }
            )
        }

        composable(NavRoutes.Stats.route) {
            StatsScreen()
        }

        composable(
            route = NavRoutes.BookDetail.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
                ?.let { java.net.URLDecoder.decode(it, "UTF-8") }
                ?: return@composable

            BookDetailScreen(
                bookId = bookId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}