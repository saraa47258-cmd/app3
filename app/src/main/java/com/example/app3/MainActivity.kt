package com.example.app3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app3.navigation.Screen
import com.example.app3.ui.screens.*
import com.example.app3.ui.theme.WishListSmartTheme
import com.example.app3.ui.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WishListSmartTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val repository = (navController.context as ComponentActivity)
        .application.let { it as com.example.app3.WishListApp }.repository

    val wishListViewModel: WishListViewModel = viewModel(
        factory = WishListViewModelFactory(repository)
    )
    val addProductViewModel: AddProductViewModel = viewModel(
        factory = AddProductViewModelFactory(repository)
    )
    val budgetViewModel: BudgetViewModel = viewModel(
        factory = BudgetViewModelFactory(repository)
    )
    val offersViewModel: OffersViewModel = viewModel(
        factory = OffersViewModelFactory(repository)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Bottom navigation items
    val bottomNavItems = listOf(
        BottomNavItem(
            route = Screen.WishList.route,
            icon = Icons.Default.List,
            label = "قائمتي"
        ),
        BottomNavItem(
            route = Screen.Budget.route,
            icon = Icons.Default.AccountBalance,
            label = "الميزانية"
        ),
        BottomNavItem(
            route = Screen.Offers.route,
            icon = Icons.Default.LocalOffer,
            label = "العروض"
        )
    )

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.WishList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ============================================
            // Main Screens (Tab Navigation)
            // ============================================
            
            composable(
                route = Screen.WishList.route,
                enterTransition = { com.example.app3.navigation.tabEnterTransition() },
                exitTransition = { com.example.app3.navigation.tabExitTransition() }
            ) {
                com.example.app3.ui.screens.WishListScreenEnhanced(
                    viewModel = wishListViewModel,
                    onNavigateToAddProduct = {
                        navController.navigate(Screen.AddProduct.route)
                    },
                    onNavigateToBudget = {
                        navController.navigate(Screen.Budget.route)
                    }
                )
            }

            composable(
                route = Screen.Budget.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.WishList.route, Screen.Offers.route -> 
                            com.example.app3.navigation.tabEnterTransition()
                        else -> 
                            com.example.app3.navigation.slideInFromLeft()
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.WishList.route, Screen.Offers.route -> 
                            com.example.app3.navigation.tabExitTransition()
                        else -> 
                            com.example.app3.navigation.slideOutToLeft()
                    }
                },
                popEnterTransition = { com.example.app3.navigation.slideInFromLeft() },
                popExitTransition = { com.example.app3.navigation.slideOutToRight() }
            ) {
                BudgetScreen(
                    viewModel = budgetViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.Offers.route,
                enterTransition = { com.example.app3.navigation.tabEnterTransition() },
                exitTransition = { com.example.app3.navigation.tabExitTransition() }
            ) {
                OffersScreen(
                    viewModel = offersViewModel
                )
            }

            // ============================================
            // Modal Screens (Slide from Bottom)
            // ============================================
            
            composable(
                route = Screen.AddProduct.route,
                enterTransition = { com.example.app3.navigation.modalEnterTransition() },
                exitTransition = { com.example.app3.navigation.fadeOut() },
                popEnterTransition = { com.example.app3.navigation.fadeIn() },
                popExitTransition = { com.example.app3.navigation.modalExitTransition() }
            ) {
                AddProductScreen(
                    viewModel = addProductViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)

