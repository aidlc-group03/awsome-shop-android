package com.awsome.shop.ui.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import com.awsome.shop.ui.components.EmptyState
import com.awsome.shop.ui.components.ErrorRetry
import com.awsome.shop.ui.components.LoadingState
import com.awsome.shop.ui.components.ProductImage
import com.awsome.shop.ui.components.clickableNoRipple
import com.awsome.shop.ui.components.formatPoints
import com.awsome.shop.ui.navigation.BottomNavBarHost
import com.awsome.shop.ui.theme.BgWhite
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onOrderClick: (Long) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPoints: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: com.awsome.shop.ui.viewmodel.OrdersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().background(BgWhite).height(48.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("兑换记录", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            }
        },
        bottomBar = {
            BottomNavBarHost(
                selectedIndex = 2,
                onNavigateToHome = onNavigateToHome,
                onNavigateToPoints = onNavigateToPoints,
                onNavigateToOrders = {},
                onNavigateToProfile = onNavigateToProfile,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            StatusFilterRow(selected = uiState.selectedStatus, onSelect = viewModel::selectStatus)
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = viewModel::refresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                when {
                    uiState.isLoading && uiState.orders.isEmpty() -> LoadingState()
                    uiState.error != null && uiState.orders.isEmpty() ->
                        ErrorRetry(message = uiState.error!!, onRetry = { viewModel.loadOrders() })
                    uiState.orders.isEmpty() -> EmptyState(message = "暂无兑换记录")
                    else -> Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        uiState.orders.forEach { order ->
                            OrderCard(order = order, onClick = { onOrderClick(order.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusFilterRow(selected: String?, onSelect: (String?) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgWhite)
            .height(44.dp)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        OrderStatusUi.filterOptions.forEach { (value, label) ->
            val isSelected = value == selected
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Primary else TextSecondary,
                modifier = Modifier.clickableNoRipple { onSelect(value) },
            )
        }
    }
}

@Composable
private fun OrderCard(order: Order, onClick: () -> Unit) {
    val (chipBg, chipText) = OrderStatusUi.chipColors(order.status)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BgWhite)
            .clickableNoRipple(onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(order.orderNo, fontSize = 12.sp, color = TextSecondary)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(chipBg)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            ) {
                Text(OrderStatusUi.displayName(order.status), fontSize = 11.sp, color = chipText)
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductImage(
                imageUrl = order.productImageUrl,
                modifier = Modifier.size(48.dp),
                cornerRadius = 8,
                iconSize = 24,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(order.productName, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary, maxLines = 2)
                Text("${formatPoints(order.pointsAmount)} 积分", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Primary)
            }
        }
    }
}
