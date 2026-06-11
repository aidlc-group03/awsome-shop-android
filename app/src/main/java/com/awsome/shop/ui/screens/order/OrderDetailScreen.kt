package com.awsome.shop.ui.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awsome.shop.data.model.Order
import com.awsome.shop.data.model.OrderStatusUi
import com.awsome.shop.ui.components.ErrorRetry
import com.awsome.shop.ui.components.LoadingState
import com.awsome.shop.ui.components.ProductImage
import com.awsome.shop.ui.components.ScreenTopBar
import com.awsome.shop.ui.components.formatPoints
import com.awsome.shop.ui.theme.BgWhite
import com.awsome.shop.ui.theme.Divider
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.PrimaryBg
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.viewmodel.OrderDetailViewModel

@Composable
fun OrderDetailScreen(
    onBack: () -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val order = uiState.order

    Scaffold(
        topBar = { ScreenTopBar(title = "订单详情", onBack = onBack) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        when {
            uiState.isLoading && order == null -> Box(Modifier.fillMaxSize().padding(padding)) { LoadingState() }
            uiState.error != null && order == null -> Box(Modifier.fillMaxSize().padding(padding)) {
                ErrorRetry(message = uiState.error!!, onRetry = viewModel::loadOrder)
            }
            order != null -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatusBanner(order)
                ProductRow(order)
                InfoCard("订单信息") {
                    InfoRow("订单编号", order.orderNo)
                    InfoRow("下单时间", order.createdAt)
                    InfoRow("兑换状态", OrderStatusUi.displayName(order.status))
                }
                InfoCard("收货信息") {
                    InfoRow("收货人", order.recipientName)
                    InfoRow("联系电话", order.recipientPhone)
                    InfoRow("收货地址", order.recipientAddress)
                }
                InfoCard("积分信息") {
                    InfoRow("商品积分", "${formatPoints(order.pointsAmount)} 积分")
                    Box(Modifier.fillMaxWidth().height(1.dp).background(Divider))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("实付积分", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text("${formatPoints(order.pointsAmount)} 积分", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBanner(order: Order) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(PrimaryBg)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Rounded.LocalShipping, null, tint = Primary, modifier = Modifier.size(24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(OrderStatusUi.displayName(order.status), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Primary)
            Text("感谢您的兑换，订单状态将实时更新", fontSize = 12.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun ProductRow(order: Order) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgWhite)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProductImage(
            imageUrl = order.productImageUrl,
            modifier = Modifier.size(56.dp),
            cornerRadius = 8,
            iconSize = 28,
        )
        Text(
            order.productName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
            maxLines = 2,
        )
        Text("${formatPoints(order.pointsAmount)} 积分", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Primary)
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        content()
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 13.sp, color = TextPrimary)
    }
}
