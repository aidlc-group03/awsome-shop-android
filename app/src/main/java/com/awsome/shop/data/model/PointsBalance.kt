package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 积分余额 (BR-A5.1, BR-A5.2)。
 */
@Serializable
data class PointsBalance(
    val balance: Int = 0,
    val totalEarned: Int = 0,
    val totalSpent: Int = 0,
)
