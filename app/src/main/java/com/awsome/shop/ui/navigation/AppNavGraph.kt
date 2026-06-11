package com.awsome.shop.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.awsome.shop.ui.screens.home.HomeScreen
import com.awsome.shop.ui.screens.login.LoginScreen
import com.awsome.shop.ui.screens.order.OrderDetailScreen
import com.awsome.shop.ui.screens.order.OrdersScreen
import com.awsome.shop.ui.screens.points.PointsCenterScreen
import com.awsome.shop.ui.screens.points.PointsHistoryScreen
import com.awsome.shop.ui.screens.product.ProductDetailScreen
import com.awsome.shop.ui.screens.profile.ProfileScreen
import com.awsome.shop.ui.screens.redemption.ConfirmRedemptionScreen
import com.awsome.shop.ui.screens.redemption.DeliveryInfoScreen
import com.awsome.shop.ui.screens.redemption.RedemptionSuccessScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: Route = Route.Login,
) {
    // 底部导航 tab 之间切换：清栈到 Home 并保留单实例。
    fun navigateTab(route: Route) {
        navController.navigate(route) {
            popUpTo(Route.Home) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Route.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Home> {
            HomeScreen(
                onProductClick = { productId -> navController.navigate(Route.ProductDetail(productId)) },
                onNavigateToPoints = { navigateTab(Route.PointsCenter) },
                onNavigateToOrders = { navigateTab(Route.Orders) },
                onNavigateToProfile = { navigateTab(Route.Profile) },
            )
        }

        composable<Route.ProductDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ProductDetail>()
            ProductDetailScreen(
                onBack = { navController.popBackStack() },
                onRedeem = { navController.navigate(Route.DeliveryInfo(route.productId)) },
            )
        }

        composable<Route.DeliveryInfo> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.DeliveryInfo>()
            DeliveryInfoScreen(
                onBack = { navController.popBackStack() },
                onNext = { name, phone, address ->
                    navController.navigate(
                        Route.ConfirmRedemption(
                            productId = route.productId,
                            recipientName = name,
                            recipientPhone = phone,
                            recipientAddress = address,
                        )
                    )
                },
            )
        }

        composable<Route.ConfirmRedemption> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ConfirmRedemption>()
            ConfirmRedemptionScreen(
                recipientName = route.recipientName,
                recipientPhone = route.recipientPhone,
                recipientAddress = route.recipientAddress,
                onBack = { navController.popBackStack() },
                onSuccess = { orderNo, orderId ->
                    navController.navigate(Route.RedemptionSuccess(orderNo, orderId)) {
                        popUpTo(Route.Home)
                    }
                },
            )
        }

        composable<Route.RedemptionSuccess> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.RedemptionSuccess>()
            RedemptionSuccessScreen(
                orderNo = route.orderNo,
                onViewOrder = {
                    navController.navigate(Route.OrderDetail(route.orderId)) {
                        popUpTo(Route.Home)
                    }
                },
                onContinueShopping = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Home) { inclusive = true }
                    }
                },
            )
        }

        composable<Route.Orders> {
            OrdersScreen(
                onOrderClick = { orderId -> navController.navigate(Route.OrderDetail(orderId)) },
                onNavigateToHome = { navigateTab(Route.Home) },
                onNavigateToPoints = { navigateTab(Route.PointsCenter) },
                onNavigateToProfile = { navigateTab(Route.Profile) },
            )
        }

        composable<Route.OrderDetail> {
            OrderDetailScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.PointsCenter> {
            PointsCenterScreen(
                onPointsHistoryClick = { navController.navigate(Route.PointsHistory) },
                onNavigateToHome = { navigateTab(Route.Home) },
                onNavigateToOrders = { navigateTab(Route.Orders) },
                onNavigateToProfile = { navigateTab(Route.Profile) },
            )
        }

        composable<Route.PointsHistory> {
            PointsHistoryScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.Profile> {
            ProfileScreen(
                onNavigateToHome = { navigateTab(Route.Home) },
                onNavigateToOrders = { navigateTab(Route.Orders) },
                onNavigateToPoints = { navigateTab(Route.PointsCenter) },
                onLogout = {
                    navController.navigate(Route.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}
