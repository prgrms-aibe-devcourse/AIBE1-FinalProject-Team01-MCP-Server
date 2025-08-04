package kr.co.amateurs.mcp.server.common.dto

enum class PaginationSortType(
    val fieldName: String
) {
    ID("id"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt")
}
