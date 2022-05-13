package com.monta.utils.paging

data class Page<T>(
    val content: List<T>,
    val pageable: Pageable,
    val totalSize: Long,
) : Iterable<T> {

    val pageNumber: Int
        get() = pageable.page

    val offset: Long
        get() = pageable.offset

    val size: Int
        get() = pageable.size

    val isEmpty: Boolean
        get() = content.isEmpty()

    val numberOfElements: Int
        get() = content.size

    override fun iterator(): Iterator<T> {
        return content.iterator()
    }
}
