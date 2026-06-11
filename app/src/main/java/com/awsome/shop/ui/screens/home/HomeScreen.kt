package com.awsome.shop.ui.screens.home

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awsome.shop.data.model.Product
import com.awsome.shop.ui.components.CategoryChip
import com.awsome.shop.ui.components.EmptyState
import com.awsome.shop.ui.components.ErrorRetry
import com.awsome.shop.ui.components.LoadingState
import com.awsome.shop.ui.components.ProductImage
import com.awsome.shop.ui.components.clickableNoRipple
import com.awsome.shop.ui.components.formatPoints
import com.awsome.shop.ui.navigation.BottomNavBarHost
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.PrimaryLight
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.theme.TextWhite
import com.awsome.shop.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Long) -> Unit,
    onNavigateToPoints: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { HomeAppBar() },
        bottomBar = {
            BottomNavBarHost(
                selectedIndex = 0,
                onNavigateToHome = {},
                onNavigateToPoints = onNavigateToPoints,
                onNavigateToOrders = onNavigateToOrders,
                onNavigateToProfile = onNavigateToProfile,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                PointsBalanceCard(onClick = onNavigateToPoints)

                CategoryRow(
                    categories = uiState.categories.map { it.name },
                    selected = uiState.selectedCategory,
                    onSelect = viewModel::selectCategory,
                )

                when {
                    uiState.isLoading && uiState.products.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxWidth().height(240.dp)) { LoadingState() }
                    }
                    uiState.error != null && uiState.products.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                            ErrorRetry(message = uiState.error!!, onRetry = { viewModel.loadProducts() })
                        }
                    }
                    uiState.products.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                            EmptyState(message = "暂无商品")
                        }
                    }
                    else -> ProductGrid(products = uiState.products, onProductClick = onProductClick)
                }
            }
        }
    }
}

@Composable
private fun HomeAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Primary)
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("AWSome Shop", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Rounded.Search, "搜索", tint = TextWhite, modifier = Modifier.size(24.dp))
            Icon(Icons.Rounded.Notifications, "通知", tint = TextWhite, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun PointsBalanceCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.horizontalGradient(listOf(Primary, PrimaryLight)))
            .clickableNoRipple(onClick)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("我的积分", fontSize = 12.sp, color = TextWhite.copy(alpha = 0.8f))
            Text("查看积分中心", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        }
        Icon(
            Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            null,
            tint = TextWhite.copy(alpha = 0.8f),
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun CategoryRow(
    categories: List<String>,
    selected: String?,
    onSelect: (String?) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CategoryChip(text = "全部", selected = selected == null, onClick = { onSelect(null) })
        categories.forEach { name ->
            CategoryChip(text = name, selected = selected == name, onClick = { onSelect(name) })
        }
    }
}

@Composable
private fun ProductGrid(products: List<Product>, onProductClick: (Long) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        products.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowItems.forEach { product ->
                    Box(modifier = Modifier.weight(1f)) {
                        ProductCard(product = product, onClick = { onProductClick(product.id) })
                    }
                }
                if (rowItems.size == 1) {
                    Box(modifier = Modifier.weight(1f)) {}
                }
            }
        }
    }
}

@Composable
private fun ProductCard(product: Product, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickableNoRipple(onClick),
    ) {
        Box {
            ProductImage(
                imageUrl = product.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                cornerRadius = 0,
            )
            if (!product.inStock) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(TextSecondary)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                ) {
                    Text("已兑完", fontSize = 11.sp, color = TextWhite)
                }
            }
        }
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = product.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                maxLines = 2,
            )
            Text(
                text = "${formatPoints(product.pointsPrice)} 积分",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary,
            )
        }
    }
}
