package com.awsome.shop.ui.screens.product

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
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.awsome.shop.data.model.Product
import com.awsome.shop.ui.components.ErrorRetry
import com.awsome.shop.ui.components.LoadingState
import com.awsome.shop.ui.components.ProductImage
import com.awsome.shop.ui.components.ScreenTopBar
import com.awsome.shop.ui.components.formatPoints
import com.awsome.shop.ui.theme.BgWhite
import com.awsome.shop.ui.theme.Border
import com.awsome.shop.ui.theme.ChipGreenBg
import com.awsome.shop.ui.theme.ChipGreenText
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.PrimaryBg
import com.awsome.shop.ui.theme.TextDisabled
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.theme.TextWhite
import com.awsome.shop.ui.viewmodel.ProductDetailViewModel

@Composable
fun ProductDetailScreen(
    onBack: () -> Unit,
    onRedeem: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val product = uiState.product

    Scaffold(
        topBar = { ScreenTopBar(title = "商品详情", onBack = onBack) },
        bottomBar = {
            if (product != null) {
                RedeemBar(
                    canRedeem = uiState.canRedeem,
                    inStock = product.inStock,
                    onRedeem = onRedeem,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        when {
            uiState.isLoading && product == null -> Box(Modifier.fillMaxSize().padding(padding)) { LoadingState() }
            uiState.error != null && product == null -> Box(Modifier.fillMaxSize().padding(padding)) {
                ErrorRetry(message = uiState.error!!, onRetry = viewModel::loadProduct)
            }
            product != null -> ProductContent(
                product = product,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
            )
        }
    }
}

@Composable
private fun ProductContent(product: Product, modifier: Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // 图片
        ProductImage(
            imageUrl = product.imageUrl,
            modifier = Modifier.fillMaxWidth().height(260.dp),
            cornerRadius = 0,
            iconSize = 100,
        )

        // 信息卡
        Column(
            modifier = Modifier.fillMaxWidth().background(BgWhite).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(product.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            product.subtitle?.takeIf { it.isNotBlank() }?.let {
                Text(it, fontSize = 13.sp, color = TextSecondary)
            }
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(formatPoints(product.pointsPrice), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Primary)
                Text("积分", fontSize = 14.sp, color = Primary, modifier = Modifier.padding(bottom = 4.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                product.deliveryMethod?.takeIf { it.isNotBlank() }?.let { TagChip(it, PrimaryBg, Primary) }
                if (product.inStock) TagChip("有货", ChipGreenBg, ChipGreenText)
                product.promotion?.takeIf { it.isNotBlank() }?.let { TagChip(it, PrimaryBg, Primary) }
            }
        }

        // 规格卡
        val specs = product.specs.orEmpty()
        if (specs.isNotEmpty() || product.brand != null) {
            Column(
                modifier = Modifier.fillMaxWidth().background(BgWhite).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("商品规格", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                product.brand?.takeIf { it.isNotBlank() }?.let { SpecRow("品牌", it) }
                product.sku.takeIf { it.isNotBlank() }?.let { SpecRow("型号", it) }
                product.colors?.takeIf { it.isNotBlank() }?.let { SpecRow("颜色", it) }
                specs.forEach { SpecRow(it.key, it.value) }
            }
        }

        product.serviceGuarantee?.takeIf { it.isNotBlank() }?.let {
            Column(
                modifier = Modifier.fillMaxWidth().background(BgWhite).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("服务保障", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Text(it, fontSize = 13.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun TagChip(text: String, bg: androidx.compose.ui.graphics.Color, fg: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(text, fontSize = 11.sp, color = fg)
    }
}

@Composable
private fun SpecRow(key: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().height(28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(key, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 13.sp, color = TextPrimary)
    }
}

@Composable
private fun RedeemBar(canRedeem: Boolean, inStock: Boolean, onRedeem: () -> Unit) {
    val label = when {
        !inStock -> "已兑完"
        !canRedeem -> "积分不足"
        else -> "立即兑换"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgWhite)
            .height(72.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = onRedeem,
            enabled = canRedeem,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                disabledContainerColor = TextDisabled,
            ),
            modifier = Modifier.fillMaxWidth().height(48.dp),
        ) {
            Text(label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
        }
    }
}
