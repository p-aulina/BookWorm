package com.example.bookworm.ui.navigation

sealed class NavRoutes(val route: String) {
    object Home: NavRoutes("home")
    object Library: NavRoutes("library")
    object Search: NavRoutes("search")
    object Stats: NavRoutes("stats")
    object BookDetail: NavRoutes("book_detail/{bookId}"){
        fun createRoute(bookId: String) = "book_detail/$bookId"
    }
}