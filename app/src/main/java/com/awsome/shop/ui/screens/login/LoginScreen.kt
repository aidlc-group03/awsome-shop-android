package com.awsome.shop.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.KeyboardOptions
import com.awsome.shop.ui.theme.Border
import com.awsome.shop.ui.theme.Error
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.theme.TextWhite
import com.awsome.shop.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (uiState.loginSuccess) {
        // 仅触发一次导航。
        androidx.compose.runtime.LaunchedEffect(Unit) { onLoginSuccess() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .verticalScroll(rememberScrollState())
            .imePadding(),
    ) {
        // Hero
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Redeem,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(56.dp),
            )
            Text(
                text = "AWSome Shop",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(top = 16.dp),
            )
            Text(
                text = "员工积分兑换平台",
                fontSize = 16.sp,
                color = TextWhite.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        // Login card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text("欢迎回来", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("登录您的账户以继续", fontSize = 14.sp, color = TextSecondary)

            LoginField(
                label = "用户名",
                value = username,
                onValueChange = { username = it },
                placeholder = "请输入用户名",
                leadingIcon = Icons.Rounded.Person,
            )
            LoginField(
                label = "密码",
                value = password,
                onValueChange = { password = it },
                placeholder = "请输入密码",
                leadingIcon = Icons.Rounded.Lock,
                isPassword = true,
            )

            uiState.error?.let { msg ->
                Text(text = msg, color = Error, fontSize = 13.sp)
            }

            Button(
                onClick = { viewModel.login(username, password) },
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                } else {
                    Text("登 录", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                }
            }

            Text(
                text = "忘记密码？",
                fontSize = 14.sp,
                color = Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun LoginField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextSecondary.copy(alpha = 0.6f)) },
            leadingIcon = { Icon(leadingIcon, null, tint = TextSecondary) },
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Border,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        )
    }
}
