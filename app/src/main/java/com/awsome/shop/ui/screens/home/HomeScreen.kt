package com.awsome.shop.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awsome.shop.ui.components.BottomNavBar
import com.awsome.shop.ui.components.CategoryChip
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.PrimaryLight
import com.awsome.shop.ui.theme.TextWhite

@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onPointsClick: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedIndex = 0,
                onItemSelected = { index ->
                    when (index) {
                        2 -> onNavigateToOrders()
                        3 -> onNavigateToProfile()
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primary)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "AWSome Shop",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                )
                Row {
                    IconButton(onClick = {}) {
                        Icon(Icons.Rounded.Search, contentDescription = "搜索", tint = TextWhite)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Rounded.Notifications, contentDescription = "通知", tint = TextWhite)
                    }
                }
            }

            // Points Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.horizontalGradient(listOf(Primary, PrimaryLight)))
                    .clickable(onClick = onPointsClick)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text("我的积分", fontSize = 12.sp, color = TextWhite.copy(alpha = 0.8f))
                        Text("12,580", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                    Icon(Icons.Rounded.ArrowForwardIos, contentDescription = null, tint = TextWhite.copy(alpha = 0.8f), modifier = Modifier.size(20.dp))
                }
            }

            // Category Filter
            val categories = listOf("全部", "数码电子", "生活日用", "办公文具")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                categories.forEachIndexed { index, cat ->
                    CategoryChip(
                        text = cat,
                        selected = index == selectedTab,
                        onClick = { selectedTab = index },
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Product Grid
            val sampleProducts = listOf(
                Triple("1", "Sony WH-1000XM5 降噪耳机", "2,580"),
                Triple("2", "Apple Watch Series 9", "3,200"),
                Triple("3", "星巴克礼品卡 200元", "680"),
                Triple("4", "小米双肩包 都市休闲款", "450"),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(sampleProducts) { (id, name, points) ->
                    ProductCard(
                        name = name,
                        points = points,
                        onClick = { onProductClick(id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(name: String, points: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Rounded.Headphones,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Primary,
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(name, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 2)
                Spacer(Modifier.height(8.dp))
                Text("$points 积分", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Primary)
            }
        }
    }
}
