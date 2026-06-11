package com.awsome.shop.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe Navigation 路由。参数对齐后端 id 类型 (Long) 与兑换流程数据传递 (BR-A7.5)。
 */
sealed interface Route {
    @Serializable data object Login : Route
    @Serializable data object Home : Route
    @Serializable data object Orders : Route
    @Serializable data object PointsCenter : Route
    @Serializable data object PointsHistory : Route
    @Serializable data object Profile : Route

    @Serializable data class ProductDetail(val productId: Long) : Route

    /** 配送信息页，携带待兑换商品 id。 */
    @Serializable data class DeliveryInfo(val productId: Long) : Route

    /** 确认兑换页，携带商品 id + 本地收集的配送信息。 */
    @Serializable data class ConfirmRedemption(
        val productId: Long,
        val recipientName: String,
        val recipientPhone: String,
        val recipientRegion: String,
        val recipientDetail: String,
    ) : Route

    /** 兑换成功页，携带订单 id（用于跳详情/展示订单号）。 */
    @Serializable data class RedemptionSuccess(
        val orderId: Long,
    ) : Route

    @Serializable data class OrderDetail(val orderId: Long) : Route
}
