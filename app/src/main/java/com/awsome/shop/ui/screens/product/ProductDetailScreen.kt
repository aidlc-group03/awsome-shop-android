package com.awsome.shop.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.PrimaryBg
import com.awsome.shop.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onBack: () -> Unit,
    onRedeem: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("商品详情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = onRedeem,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    ) {
                        Text("立即兑换", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(PrimaryBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.Headphones, contentDescription = null, modifier = Modifier.size(100.dp), tint = Primary)
            }

            // Product Info
            Surface(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Sony WH-1000XM5 降噪耳机", style = MaterialTheme.typography.titleLarge)
                    Text("全新一代行业领先降噪 | 30小时续航 | 轻量舒适佩戴", style = MaterialTheme.typography.bodySmall)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("2,580", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Primary)
                        Spacer(Modifier.padding(start = 4.dp))
                        Text("积分", fontSize = 14.sp, color = Primary)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Specs
            Surface(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("商品规格", style = MaterialTheme.typography.titleSmall)
                    val specs = listOf("品牌" to "Sony", "型号" to "WH-1000XM5", "颜色" to "黑色", "连接方式" to "蓝牙 5.2", "重量" to "250g")
                    specs.forEach { (label, value) ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(label, style = MaterialTheme.typography.bodySmall)
                            Text(value, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
