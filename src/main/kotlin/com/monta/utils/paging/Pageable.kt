package com.monta.utils.paging

data class Pageable(
    val page: Int,
    val size: Int,
    val sort: Sort,
) {
    init {
        require(page >= 0) { "page number must be greater or equal to zero" }
        require(size != 0) { "pagination size must be greater than zero" }
    }

    val offset: Long
        get() = (page.toLong() * size.toLong())
}
