package com.monta.utils.paging

data class Order(
    // property name to order by
    val property: String,
    // The direction order by
    val direction: Direction = Direction.ASC,
    // ignore case when sorting
    val ignoreCase: Boolean = false,
) {

    val isAscending: Boolean
        get() = direction == Direction.ASC

    enum class Direction {
        ASC, DESC
    }
}
