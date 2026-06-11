package com.awsome.shop.ui.screens.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Toll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.awsome.shop.ui.theme.Error
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.PrimaryBg
import com.awsome.shop.ui.theme.PrimaryLight
import com.awsome.shop.ui.theme.TextDisabled
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.theme.TextWhite
import com.awsome.shop.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToPoints: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.loggedOut) {
        if (uiState.loggedOut) onLogout()
    }

    Scaffold(
        bottomBar = {
            BottomNavBarHost(
                selectedIndex = 3,
                onNavigateToHome = onNavigateToHome,
                onNavigateToPoints = onNavigateToPoints,
                onNavigateToOrders = onNavigateToOrders,
                onNavigateToProfile = {},
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
            ProfileHeader(
                name = uiState.user?.displayName?.ifBlank { uiState.user?.username ?: "" } ?: "未登录",
                role = uiState.user?.role ?: "",
                account = uiState.user?.username ?: "",
            )

            // 积分统计
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWhite)
                    .clickableNoRipple(onNavigateToPoints)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceAround) {
                    StatCol(formatPoints(uiState.balance?.balance ?: 0), "可用积分")
                    StatCol(formatPoints(uiState.balance?.totalEarned ?: 0), "累计获得")
                    StatCol(formatPoints(uiState.balance?.totalSpent ?: 0), "已使用")
                }
                Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = TextDisabled, modifier = Modifier.size(20.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(BgWhite),
                ) {
                    MenuRow(Icons.AutoMirrored.Rounded.ReceiptLong, "我的订单", "查看全部兑换记录", onNavigateToOrders)
                    MenuRow(Icons.Rounded.AccountBalanceWallet, "积分明细", "查看积分收支", onNavigateToPoints)
                    MenuRow(Icons.Rounded.Toll, "积分中心", "积分规则与获取途径", onNavigateToPoints, showDivider = false)
                }
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(BgWhite),
                ) {
                    MenuRow(Icons.Rounded.Help, "帮助中心", "常见问题与反馈", {})
                    MenuRow(Icons.Rounded.Info, "关于", "版本 1.0.0", {}, showDivider = false)
                }

                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Error),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                ) {
                    Icon(Icons.Rounded.Logout, null, tint = Error, modifier = Modifier.size(20.dp))
                    Text("  退出登录", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Error)
                }
            }
        }
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout()
            },
            onDismiss = { showLogoutDialog = false },
        )
    }
}

@Composable
private fun ProfileHeader(name: String, role: String, account: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Brush.verticalGradient(listOf(Primary, PrimaryLight)))
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier.size(56.dp).clip(CircleShape).background(TextWhite.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = name.take(1),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite,
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(name, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
            if (role.isNotBlank()) Text(role, fontSize = 13.sp, color = TextWhite.copy(alpha = 0.8f))
            if (account.isNotBlank()) Text("账号：$account", fontSize = 12.sp, color = TextWhite.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun StatCol(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Primary)
        Text(label, fontSize = 12.sp, color = TextSecondary)
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    showDivider: Boolean = true,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickableNoRipple(onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape).background(PrimaryBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, tint = Primary, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            Text(subtitle, fontSize = 11.sp, color = TextSecondary)
        }
        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = TextDisabled, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("确认退出登录？", fontWeight = FontWeight.SemiBold) },
        text = { Text("退出后需要重新输入账号密码登录", color = TextSecondary) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("确认退出", color = Error, fontWeight = FontWeight.SemiBold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消", color = TextPrimary) }
        },
    )
}
