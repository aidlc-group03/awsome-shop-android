package com.awsome.shop.ui.screens.points

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.TrendingUp
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
import com.awsome.shop.data.model.PointsTransaction
import com.awsome.shop.data.model.PointsTypeUi
import com.awsome.shop.ui.components.EmptyState
import com.awsome.shop.ui.components.ErrorRetry
import com.awsome.shop.ui.components.LoadingState
import com.awsome.shop.ui.components.ScreenTopBar
import com.awsome.shop.ui.components.clickableNoRipple
import com.awsome.shop.ui.components.formatPoints
import com.awsome.shop.ui.theme.BgWhite
import com.awsome.shop.ui.theme.ChipGreenBg
import com.awsome.shop.ui.theme.ChipRedBg
import com.awsome.shop.ui.theme.Divider
import com.awsome.shop.ui.theme.Error
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.Success
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.viewmodel.PointsViewModel

@Composable
fun PointsHistoryScreen(
    onBack: () -> Unit,
    viewModel: PointsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { ScreenTopBar(title = "积分明细", onBack = onBack) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // 类型筛选
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
                PointsTypeUi.filterOptions.forEach { (value, label) ->
                    val isSelected = value == uiState.selectedType
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) Primary else TextSecondary,
                        modifier = Modifier.clickableNoRipple { viewModel.selectType(value) },
                    )
                }
            }

            when {
                uiState.isLoading && uiState.transactions.isEmpty() -> LoadingState()
                uiState.error != null && uiState.transactions.isEmpty() ->
                    ErrorRetry(message = uiState.error!!, onRetry = { viewModel.loadTransactions() })
                uiState.transactions.isEmpty() -> EmptyState(message = "暂无积分记录")
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                ) {
                    uiState.transactions.forEach { tx ->
                        TransactionItem(tx)
                        Box(Modifier.fillMaxWidth().height(1.dp).background(Divider))
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(tx: PointsTransaction) {
    val isIncome = PointsTypeUi.isIncome(tx.type)
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isIncome) ChipGreenBg else ChipRedBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (isIncome) Icons.Rounded.TrendingUp else Icons.Rounded.ShoppingBag,
                contentDescription = null,
                tint = if (isIncome) Success else Error,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                tx.description ?: PointsTypeUi.displayName(tx.type),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                maxLines = 2,
            )
            Text(tx.createdAt, fontSize = 11.sp, color = TextSecondary)
        }
        Text(
            text = (if (isIncome) "+" else "-") + formatPoints(kotlin.math.abs(tx.amount)),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isIncome) Success else Error,
        )
    }
}
