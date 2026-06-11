package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 积分余额 — 对齐后端 PointsBalance (BR-A5.1, BR-A5.2)。
 */
@Serializable
data class PointsBalance(
    val userId: Long = 0,
    val balance: Int = 0,
    val totalEarned: Int = 0,
    val totalUsed: Int = 0,
    val redemptionCount: Int = 0,
)
