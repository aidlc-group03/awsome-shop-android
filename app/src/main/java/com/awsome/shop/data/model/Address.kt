package com.awsome.shop.data.model

import kotlinx.serialization.Serializable

/** 收货地址 — 对齐后端 AddressDTO。 */
@Serializable
data class Address(
    val id: Long,
    val name: String = "",
    val phone: String = "",
    val region: String = "",
    val detail: String = "",
    val isDefault: Boolean = false,
)

/** 新增/编辑收货地址请求。 */
@Serializable
data class AddressRequest(
    val name: String,
    val phone: String,
    val region: String,
    val detail: String,
    val isDefault: Boolean = false,
)
