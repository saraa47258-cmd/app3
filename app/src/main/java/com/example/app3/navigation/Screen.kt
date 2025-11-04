package com.example.app3.navigation

sealed class Screen(val route: String) {
    object WishList : Screen("wishlist")
    object AddProduct : Screen("add_product")
    object Budget : Screen("budget")
    object Offers : Screen("offers")
}



