package com.monta.utils.exposed

import com.monta.utils.paging.Page
import com.monta.utils.paging.Pageable
import org.intellij.lang.annotations.Language
import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.Transaction
import java.sql.ResultSet

fun <T> SizedIterable<T>.asPageable(
    pageable: Pageable,
    totalCount: () -> Long,
    modifiers: (SizedIterable<T>.() -> List<T>)? = null,
): Page<T> {

    val value = modifiers?.invoke(
        limit(pageable.size, pageable.offset)
    ) ?: limit(pageable.size, pageable.offset).toList()

    return Page(
        value,
        pageable,
        totalCount()
    )
}

/**
 * Allows you to write your own custom query and get back a result list in a DAO list
 *
 * Example can be found below:
 *
 * <pre>
 * val result: List<CountryAreaDAO> = execAndMap(
 *      stmt = "select ${CountryAreas.allFields} from country_areas where true = ST_CONTAINS(area, Point(?,?))",
 *      args = listOf(
 *          DoubleColumnType() to longitude,
 *          DoubleColumnType() to latitude
 *      ),
 *      transform = { resultSet ->
 *          CountryAreaDAO.wrapRow(
 *              ResultRow.create(resultSet, CountryAreas.fieldsIndex)
 *          )
 *      }
 * )
 * </pre>
 */
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
