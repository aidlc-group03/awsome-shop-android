package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/**
 * 分页结果 — 对齐后端 MyBatis-Plus 分页字段 (records/current/pages)。
 */
@Serializable
data class PageResult<T>(
    val records: List<T> = emptyList(),
    val total: Int = 0,
    val current: Int = 1,
    val size: Int = 20,
    val pages: Int = 0,
) {
    /** 列表数据别名，供 UI/仓库统一访问。 */
    val list: List<T> get() = records

    /** 是否还有下一页。 */
    val hasMore: Boolean get() = current < pages
}
