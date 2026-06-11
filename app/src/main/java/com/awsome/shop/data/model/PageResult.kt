package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 分页结果 (BR-A2.5)。
 */
@Serializable
data class PageResult<T>(
    val list: List<T> = emptyList(),
    val total: Int = 0,
    val page: Int = 1,
    val size: Int = 20,
) {
    /** 是否还有下一页。 */
    val hasMore: Boolean get() = page * size < total
}
