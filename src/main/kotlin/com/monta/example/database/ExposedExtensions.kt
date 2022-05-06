package com.monta.example.database

import org.intellij.lang.annotations.Language
import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.Transaction
import java.sql.ResultSet

// fun <T> SizedIterable<T>.asPageable(
//    pageable: Pageable,
//    totalCount: () -> Long,
//    modifiers: (SizedIterable<T>.() -> List<T>)? = null,
// ): Page<T> {
//
//    val value = modifiers?.invoke(
//        limit(pageable.size, pageable.offset)
//    ) ?: limit(pageable.size, pageable.offset).toList()
//
//    return Page.of(
//        value,
//        pageable,
//        totalCount()
//    )
// }

fun <T : Any> Transaction.execAndMap(
    @Language("sql")
    stmt: String,
    args: Iterable<Pair<IColumnType, Any?>> = emptyList(),
    transform: (ResultSet) -> T,
): List<T> {
    val result = arrayListOf<T>()
    exec(stmt, args) { rs ->
        while (rs.next()) {
            result += transform(rs)
        }
    }
    return result
}
