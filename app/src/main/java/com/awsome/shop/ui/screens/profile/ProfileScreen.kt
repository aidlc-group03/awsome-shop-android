package com.awsome.shop.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToPointsCenter: () -> Unit,
    onNavigateToPointsHistory: () -> Unit,
    onNavigateToDeliveryInfo: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("我的 - TODO")
    }
}
