package com.awsome.shop.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 底部导航项。索引顺序即展示顺序：商城 / 积分 / 订单 / 我的。
 */
enum class BottomNavItem(
    val route: Route,
    val icon: ImageVector,
    val label: String,
) {
    Shop(Route.Home, Icons.Rounded.Storefront, "商城"),
    Points(Route.PointsCenter, Icons.Rounded.Stars, "积分"),
    Orders(Route.Orders, Icons.Rounded.ReceiptLong, "订单"),
    Profile(Route.Profile, Icons.Rounded.AccountCircle, "我的"),
}
