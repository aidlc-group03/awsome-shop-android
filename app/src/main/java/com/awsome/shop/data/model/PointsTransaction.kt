package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 积分交易记录 — 对齐后端字段。type 为字符串：
 * earn / spend / refund / admin_add / admin_deduct。
 */
@Serializable
data class PointsTransaction(
    val id: Long,
    val type: String = "earn",
    val points: Int = 0,
    val balanceAfter: Int = 0,
    val description: String? = null,
    val createdAt: String = "",
)

/**
 * 积分交易类型展示工具 (BR-A5.3, BR-A5.5)。
 */
object PointsTypeUi {
    fun displayName(type: String): String = when (type.lowercase()) {
        "earn" -> "积分获得"
        "spend" -> "积分消费"
        "refund" -> "积分退还"
        "admin_add" -> "管理员发放"
        "admin_deduct" -> "管理员扣减"
        else -> type
    }

    /** earn/refund/admin_add 为收入(+)，其余为支出(-)。 */
    fun isIncome(type: String): Boolean = when (type.lowercase()) {
        "earn", "refund", "admin_add" -> true
        else -> false
    }

    /** 可筛选的类型列表 (value 为 null 表示"全部")。 */
    val filterOptions: List<Pair<String?, String>> = listOf(
        null to "全部",
        "earn" to "获得",
        "spend" to "消费",
        "refund" to "退还",
    )
}
