package com.awsome.shop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.awsome.shop.ui.theme.BgWhite
import com.awsome.shop.ui.theme.Border
import com.awsome.shop.ui.theme.Primary
import com.awsome.shop.ui.theme.PrimaryBg
import com.awsome.shop.ui.theme.TextPrimary

/** 千分位格式化积分数值。 */
fun formatPoints(value: Int): String = "%,d".format(value)

/** 无涟漪点击修饰。 */
fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier =
    this.composed {
        clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
        )
    }

/** 通用顶部栏：返回箭头 + 标题，底部分割线。 */
@Composable
fun ScreenTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgWhite)
            .height(48.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (onBack != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "返回",
                tint = TextPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .clickableNoRipple(onBack),
            )
        }
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
        )
        trailing?.invoke()
    }
}

/**
 * 商品图片：有 url 用 Coil 加载，否则展示彩色占位 + 购物袋图标。
 */
@Composable
fun ProductImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    cornerRadius: Int = 12,
    iconSize: Int = 48,
    placeholderBg: Color = PrimaryBg,
    placeholderIcon: ImageVector = Icons.Rounded.ShoppingBag,
) {
    val shape = RoundedCornerShape(cornerRadius.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(placeholderBg),
        contentAlignment = Alignment.Center,
    ) {
        if (!imageUrl.isNullOrBlank()) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Icon(placeholderIcon, null, tint = Primary, modifier = Modifier.size(iconSize.dp))
                },
                error = {
                    Icon(placeholderIcon, null, tint = Primary, modifier = Modifier.size(iconSize.dp))
                },
            )
        } else {
            Icon(
                imageVector = placeholderIcon,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(iconSize.dp),
            )
        }
    }
}

/** 边框卡片背景修饰。 */
fun Modifier.cardBorder(cornerRadius: Int = 12): Modifier =
    this.clip(RoundedCornerShape(cornerRadius.dp))
