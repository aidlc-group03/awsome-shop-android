package com.awsome.shop.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.awsome.shop.ui.navigation.AppNavGraph
import com.awsome.shop.ui.navigation.Route

@Composable
fun AwsomeShopRoot(startDestination: Route = Route.Login) {
    val navController = rememberNavController()
    AppNavGraph(
        navController = navController,
        startDestination = startDestination,
    )
}
