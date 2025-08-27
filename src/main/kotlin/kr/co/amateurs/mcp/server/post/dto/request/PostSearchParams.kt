package kr.co.amateurs.mcp.server.post.dto.request

import jakarta.validation.constraints.NotBlank
import kr.co.amateurs.mcp.server.common.dto.PaginationParam
import kr.co.amateurs.mcp.server.common.dto.PaginationSortType
import org.springframework.data.domain.Sort

class PostSearchParams(
    @field:NotBlank(message = "키워드 혹은 닉네임 없이 비어있는 값을 입력할 수 없습니다")
    val keyword: String,
    override val pageNumber: Int = 0,
    override val pageSize: Int = 10,
    override val direction: Sort.Direction = Sort.Direction.DESC,
    override val field: PaginationSortType = PaginationSortType.CREATED_AT
) : PaginationParam
