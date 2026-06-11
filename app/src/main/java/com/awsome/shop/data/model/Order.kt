package com.awsome.shop.data.model

import androidx.compose.ui.graphics.Color
import com.awsome.shop.ui.theme.ChipBlueBg
import com.awsome.shop.ui.theme.ChipBlueText
import com.awsome.shop.ui.theme.ChipGreenBg
import com.awsome.shop.ui.theme.ChipGreenText
import com.awsome.shop.ui.theme.ChipOrangeBg
import com.awsome.shop.ui.theme.ChipOrangeText
import com.awsome.shop.ui.theme.ChipRedBg
import com.awsome.shop.ui.theme.ChipRedText
import kotlinx.serialization.Serializable

/**
 * 订单模型 — 对齐后端字段。status 为字符串：
 * pending / confirmed / shipping / completed / cancelled。
 */
@Serializable
data class Order(
    val id: Long,
    val orderNo: String,
    val productId: Long = 0,
    val productName: String = "",
    val productImageUrl: String? = null,
    val pointsAmount: Int = 0,
    val quantity: Int = 1,
    val status: String = "pending",
    val recipientName: String = "",
    val recipientPhone: String = "",
    val recipientAddress: String = "",
    val createdAt: String = "",
)

/**
 * 订单状态展示映射工具 (BR-A4.2)。
 */
object OrderStatusUi {
    fun displayName(status: String): String = when (status.lowercase()) {
        "pending" -> "待确认"
        "confirmed" -> "待发货"
        "shipping" -> "配送中"
        "completed" -> "已完成"
        "cancelled" -> "已取消"
        else -> status
    }

    /** 返回 (背景色, 文字色) 用于状态 Chip。 */
    fun chipColors(status: String): Pair<Color, Color> = when (status.lowercase()) {
        "pending" -> ChipOrangeBg to ChipOrangeText
        "confirmed" -> ChipBlueBg to ChipBlueText
        "shipping" -> ChipBlueBg to ChipBlueText
        "completed" -> ChipGreenBg to ChipGreenText
        "cancelled" -> ChipRedBg to ChipRedText
        else -> ChipBlueBg to ChipBlueText
    }

    /** 可筛选的状态列表 (value 为 null 表示"全部")。 */
    val filterOptions: List<Pair<String?, String>> = listOf(
        null to "全部",
        "pending" to "待确认",
        "confirmed" to "待发货",
        "shipping" to "配送中",
        "completed" to "已完成",
        "cancelled" to "已取消",
    )
}
