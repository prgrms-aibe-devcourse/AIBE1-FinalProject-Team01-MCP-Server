package kr.co.amateurs.mcp.server.common.dto

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

interface PaginationParam {
    val pageNumber: Int
    val pageSize: Int
    val direction: Sort.Direction
    val field: PaginationSortType

    fun toPageable(): Pageable {
        return PageRequest.of(pageNumber, pageSize, direction, field.fieldName)
    }
}