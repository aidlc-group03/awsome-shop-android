package com.awsome.shop.ui.screens.redemption

import android.widget.Toast
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
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awsome.shop.ui.components.LoadingState
import com.awsome.shop.ui.components.ProductImage
import com.awsome.shop.ui.components.ScreenTopBar
import com.awsome.shop.ui.components.formatPoints
import com.awsome.shop.ui.theme.BgWhite
import com.awsome.shop.ui.theme.Divider
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.theme.TextWhite
import com.awsome.shop.ui.viewmodel.ProductDetailViewModel
import com.awsome.shop.ui.viewmodel.RedemptionViewModel

@Composable
fun ConfirmRedemptionScreen(
    recipientName: String,
    recipientPhone: String,
    recipientAddress: String,
    onBack: () -> Unit,
    onSuccess: (orderNo: String, orderId: Long) -> Unit,
    productViewModel: ProductDetailViewModel = hiltViewModel(),
    redemptionViewModel: RedemptionViewModel = hiltViewModel(),
) {
    val productState by productViewModel.uiState.collectAsStateWithLifecycle()
    val redeemState by redemptionViewModel.uiState.collectAsStateWithLifecycle()
    val product = productState.product
    val context = LocalContext.current

    LaunchedEffect(redeemState.orderResult) {
        redeemState.orderResult?.let { onSuccess(it.orderNo, it.id) }
    }
    LaunchedEffect(redeemState.error) {
        redeemState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            redemptionViewModel.consumeError()
        }
    }

    Scaffold(
        topBar = { ScreenTopBar(title = "确认兑换", onBack = onBack) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWhite)
                    .height(72.dp)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Button(
                    onClick = {
                        product?.let {
                            redemptionViewModel.createOrder(it.id, recipientName, recipientPhone, recipientAddress)
                        }
                    },
                    enabled = product != null && !redeemState.isSubmitting,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                ) {
                    if (redeemState.isSubmitting) {
                        CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                    } else {
                        Text("确认兑换", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        if (product == null) {
            Box(Modifier.fillMaxSize().padding(padding)) { LoadingState() }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 商品摘要
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWhite, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ProductImage(
                    imageUrl = product.imageUrl,
                    modifier = Modifier.size(64.dp),
                    cornerRadius = 8,
                    iconSize = 32,
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(product.name, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextPrimary, maxLines = 2)
                    product.colors?.takeIf { it.isNotBlank() }?.let {
                        Text("颜色：$it", fontSize = 13.sp, color = TextSecondary)
                    }
                }
            }

            // 积分明细
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWhite, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("积分明细", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                DetailRow("商品积分", "${formatPoints(product.pointsPrice)} 积分")
                DetailRow("数量", "×1")
                DetailRow("运费", "包邮")
                Box(Modifier.fillMaxWidth().height(1.dp).background(Divider))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("应付积分", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text("${formatPoints(product.pointsPrice)} 积分", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Primary)
                }
                productState.balance?.let { balance ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("当前余额", fontSize = 12.sp, color = TextSecondary)
                        Text("${formatPoints(balance.balance)} 积分", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }

            // 收货信息
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWhite, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("收货信息", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Rounded.LocationOn, null, tint = Primary, modifier = Modifier.size(18.dp))
                    Text("$recipientName  $recipientPhone", fontSize = 14.sp, color = TextPrimary)
                }
                Text(recipientAddress, fontSize = 13.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 14.sp, color = TextSecondary)
        Text(value, fontSize = 14.sp, color = TextPrimary)
    }
}
