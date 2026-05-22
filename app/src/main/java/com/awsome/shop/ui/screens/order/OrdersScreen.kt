package com.awsome.shop.ui.screens.order

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun OrdersScreen(
    onBack: () -> Unit,
    onOrderClick: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("兑换记录 - TODO")
    }
}
