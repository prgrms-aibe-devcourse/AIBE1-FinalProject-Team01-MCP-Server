package kr.co.amateurs.mcp.server.post.dto.response

data class PostResponseDTO(
    val id: Long,
    val boardType: String,
    val title: String,
    val content: String,
    val nickname: String,
    val tags: String,
    val createdAt: String,
    val updatedAt: String
)