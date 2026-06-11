package com.awsome.shop.ui.screens.points

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awsome.shop.ui.components.clickableNoRipple
import com.awsome.shop.ui.components.formatPoints
import com.awsome.shop.ui.navigation.BottomNavBarHost
import com.awsome.shop.ui.theme.BgWhite
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.PrimaryBg
import com.awsome.shop.ui.theme.PrimaryLight
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.theme.TextWhite
import com.awsome.shop.ui.viewmodel.PointsViewModel

@Composable
fun PointsCenterScreen(
    onPointsHistoryClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: PointsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val balance = uiState.balance

    Scaffold(
        bottomBar = {
            BottomNavBarHost(
                selectedIndex = 1,
                onNavigateToHome = onNavigateToHome,
                onNavigateToPoints = {},
                onNavigateToOrders = onNavigateToOrders,
                onNavigateToProfile = onNavigateToProfile,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            // 渐变头部
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Brush.verticalGradient(listOf(Primary, PrimaryLight)))
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("当前可用积分", fontSize = 14.sp, color = TextWhite.copy(alpha = 0.8f))
                Text(
                    formatPoints(balance?.balance ?: 0),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    HeaderStat(formatPoints(balance?.totalEarned ?: 0), "累计获得")
                    HeaderStat(formatPoints(balance?.totalUsed ?: 0), "已使用")
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // 快捷入口
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BgWhite)
                        .padding(vertical = 16.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    QuickAction(Icons.Rounded.History, "积分明细", onPointsHistoryClick)
                    QuickAction(Icons.Rounded.CardGiftcard, "兑换记录", onNavigateToOrders)
                    QuickAction(Icons.Rounded.EmojiEvents, "积分攻略") {}
                    QuickAction(Icons.Rounded.Help, "常见问题") {}
                }

                // 积分获取途径
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BgWhite)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("积分获取途径", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    EarnRow("工龄积分", "每满1年发放", "+1,000/年")
                    EarnRow("绩效奖励", "季度绩效评估", "+500~2,000")
                    EarnRow("节日福利", "法定节假日", "+200~800")
                    EarnRow("特别贡献", "项目突出表现", "+500~5,000")
                }
            }
        }
    }
}

@Composable
private fun HeaderStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
        Text(label, fontSize = 11.sp, color = TextWhite.copy(alpha = 0.7f))
    }
}

@Composable
private fun QuickAction(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clickableNoRipple(onClick),
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(PrimaryBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, tint = Primary, modifier = Modifier.size(20.dp))
        }
        Text(label, fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
private fun EarnRow(title: String, desc: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth().height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            Text(desc, fontSize = 11.sp, color = TextSecondary)
        }
        Text(amount, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Primary)
    }
}
