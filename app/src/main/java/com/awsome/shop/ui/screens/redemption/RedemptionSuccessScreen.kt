package com.awsome.shop.ui.screens.redemption

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awsome.shop.ui.components.ScreenTopBar
import com.awsome.shop.ui.theme.BgWhite
import com.awsome.shop.ui.theme.ChipGreenBg
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.Success
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.theme.TextWhite

@Composable
fun RedemptionSuccessScreen(
    orderNo: String,
    onViewOrder: () -> Unit,
    onContinueShopping: () -> Unit,
) {
    Scaffold(
        topBar = { ScreenTopBar(title = "兑换结果") },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(ChipGreenBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.CheckCircle, null, tint = Success, modifier = Modifier.size(48.dp))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("兑换成功！", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(
                    "您的订单已提交，我们将尽快为您发货",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWhite, RoundedCornerShape(12.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SuccessRow("订单编号", orderNo)
                SuccessRow("兑换状态", "待确认")
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = onViewOrder,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                ) {
                    Text("查看订单详情", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                }
                OutlinedButton(
                    onClick = onContinueShopping,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                ) {
                    Text("继续兑换", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                }
            }
        }
    }
}

@Composable
private fun SuccessRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
    }
}
