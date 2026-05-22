package com.awsome.shop.ui.screens.redemption

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RedemptionSuccessScreen(
    onViewOrder: (String) -> Unit,
    onContinueShopping: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("兑换成功 - TODO")
    }
}
