package com.awsome.shop.ui.screens.points

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PointsCenterScreen(
    onBack: () -> Unit,
    onPointsHistoryClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToOrders: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("积分中心 - TODO")
    }
}
