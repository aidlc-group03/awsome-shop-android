package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 积分交易记录 — 对齐后端 PointsTransactionDTO。
 * type: REDEMPTION / PERFORMANCE / SENIORITY / HOLIDAY / SPECIAL / ADMIN_ADD / ADMIN_DEDUCT / REFUND。
 */
@Serializable
data class PointsTransaction(
    val id: Long,
    val type: String = "",
    val amount: Int = 0,
    val balanceAfter: Int = 0,
    val description: String? = null,
    val relatedOrderId: Long? = null,
    val createdAt: String = "",
)

/**
 * 积分交易类型展示工具 (BR-A5.3, BR-A5.5)。
 */
object PointsTypeUi {
    fun displayName(type: String): String = when (type.uppercase()) {
        "REDEMPTION" -> "积分兑换"
        "PERFORMANCE" -> "绩效奖励"
        "SENIORITY" -> "工龄积分"
        "HOLIDAY" -> "节日福利"
        "SPECIAL" -> "特别贡献"
        "ADMIN_ADD" -> "管理员发放"
        "ADMIN_DEDUCT" -> "管理员扣减"
        "REFUND" -> "积分退还"
        else -> type
    }

    /** 获得类（绿色 +）：除兑换/管理员扣减外均为收入。 */
    fun isIncome(type: String): Boolean = when (type.uppercase()) {
        "REDEMPTION", "ADMIN_DEDUCT" -> false
        else -> true
    }

    /** 可筛选的类型列表 (value 为 null 表示"全部")。 */
    val filterOptions: List<Pair<String?, String>> = listOf(
        null to "全部",
        "REDEMPTION" to "兑换",
        "PERFORMANCE" to "绩效",
        "SENIORITY" to "工龄",
        "HOLIDAY" to "福利",
        "REFUND" to "退还",
    )
}
