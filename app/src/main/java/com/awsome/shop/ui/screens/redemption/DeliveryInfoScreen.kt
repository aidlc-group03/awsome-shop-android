package com.awsome.shop.ui.screens.redemption

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awsome.shop.ui.components.ScreenTopBar
import com.awsome.shop.ui.theme.BgWhite
import com.awsome.shop.ui.theme.Border
import com.awsome.shop.ui.theme.Error
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.TextPrimary
import com.awsome.shop.ui.theme.TextSecondary
import com.awsome.shop.ui.theme.TextWhite

@Composable
fun DeliveryInfoScreen(
    onBack: () -> Unit,
    onNext: (name: String, phone: String, address: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    fun submit() {
        when {
            name.isBlank() -> error = "请输入收货人姓名"
            !phone.matches(Regex("^\\d{11}$")) -> error = "请输入正确的 11 位手机号"
            detail.isBlank() -> error = "请输入详细地址"
            else -> {
                error = null
                val fullAddress = listOf(region.trim(), detail.trim()).filter { it.isNotEmpty() }.joinToString(" ")
                onNext(name.trim(), phone.trim(), fullAddress)
            }
        }
    }

    Scaffold(
        topBar = { ScreenTopBar(title = "填写收货信息", onBack = onBack) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWhite)
                    .height(72.dp)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Button(
                    onClick = ::submit,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                ) {
                    Text("保存并使用此地址", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWhite, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("新增收货地址", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                FormField("收货人姓名", name, { name = it }, "请输入姓名")
                FormField("手机号码", phone, { phone = it.filter(Char::isDigit).take(11) }, "请输入手机号", KeyboardType.Phone)
                FormField("所在地区", region, { region = it }, "省/市/区")
                FormField("详细地址", detail, { detail = it }, "街道、楼栋、门牌号")
            }
            error?.let { Text(it, color = Error, fontSize = 13.sp) }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextSecondary.copy(alpha = 0.6f)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Border,
            ),
            modifier = Modifier.fillMaxWidth().height(52.dp),
        )
    }
}
