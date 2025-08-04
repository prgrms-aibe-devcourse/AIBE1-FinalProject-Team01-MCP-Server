package kr.co.amateurs.mcp.server.post.dto.request

import kr.co.amateurs.mcp.server.common.dto.PaginationParam
import kr.co.amateurs.mcp.server.common.dto.PaginationSortType
import org.springframework.data.domain.Sort

class PostSearchParams(
    val keyword: String,
    override val pageNumber: Int = 0,
    override val pageSize: Int = 10,
    override val direction: Sort.Direction = Sort.Direction.DESC,
    override val field: PaginationSortType = PaginationSortType.CREATED_AT
) : PaginationParam
