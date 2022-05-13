package com.monta.utils.paging

data class Sort(
    val orderBy: List<Order>?,
) {
    val isSorted: Boolean
        get() = !orderBy.isNullOrEmpty()
}
