package com.awsome.shop.ui.navigation

import androidx.compose.runtime.Composable
import com.awsome.shop.ui.components.BottomNavBar

/**
 * 底部导航宿主：将 tab 索引映射到对应的导航回调。
 * 索引顺序：商城=0 / 积分=1 / 订单=2 / 我的=3 (见 [BottomNavItem])。
 */
@Composable
fun BottomNavBarHost(
    selectedIndex: Int,
    onNavigateToHome: () -> Unit,
    onNavigateToPoints: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    BottomNavBar(
        selectedIndex = selectedIndex,
        onItemSelected = { index ->
            if (index == selectedIndex) return@BottomNavBar
            when (index) {
                0 -> onNavigateToHome()
                1 -> onNavigateToPoints()
                2 -> onNavigateToOrders()
                3 -> onNavigateToProfile()
            }
        },
    )
}
