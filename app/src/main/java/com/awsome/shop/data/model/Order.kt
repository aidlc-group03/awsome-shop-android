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
 * 订单模型 — 对齐后端 OrderDTO / OrderDetailDTO。
 * status: PENDING / SHIPPED / COMPLETED / CANCELLED。
 * 列表接口不含 address；详情接口包含 address 与 updatedAt。
 */
@Serializable
data class Order(
    val id: Long,
    val productName: String = "",
    val productImageUrl: String? = null,
    val points: Int = 0,
    val status: String = "PENDING",
    val trackingNumber: String? = null,
    val createdAt: String = "",
    // 详情字段
    val address: AddressSnapshot? = null,
    val updatedAt: String? = null,
) {
    /** 展示用订单号（后端无 orderNo，使用 id）。 */
    val orderNo: String get() = "#$id"
}

@Serializable
data class AddressSnapshot(
    val name: String = "",
    val phone: String = "",
    val region: String = "",
    val detail: String = "",
) {
    val fullAddress: String get() = listOf(region, detail).filter { it.isNotBlank() }.joinToString(" ")
}

/**
 * 订单状态展示映射工具 (BR-A4.2)。
 */
object OrderStatusUi {
    fun displayName(status: String): String = when (status.uppercase()) {
        "PENDING" -> "待发货"
        "SHIPPED" -> "已发货"
        "COMPLETED" -> "已完成"
        "CANCELLED" -> "已取消"
        else -> status
    }

    /** 返回 (背景色, 文字色) 用于状态 Chip。 */
    fun chipColors(status: String): Pair<Color, Color> = when (status.uppercase()) {
        "PENDING" -> ChipOrangeBg to ChipOrangeText
        "SHIPPED" -> ChipBlueBg to ChipBlueText
        "COMPLETED" -> ChipGreenBg to ChipGreenText
        "CANCELLED" -> ChipRedBg to ChipRedText
        else -> ChipBlueBg to ChipBlueText
    }

    /** 可筛选的状态列表 (value 为 null 表示"全部")。 */
    val filterOptions: List<Pair<String?, String>> = listOf(
        null to "全部",
        "PENDING" to "待发货",
        "SHIPPED" to "已发货",
        "COMPLETED" to "已完成",
        "CANCELLED" to "已取消",
    )
}
