package kr.co.amateurs.mcp.server.post.dto.response

import java.time.LocalDateTime

data class PostResponseDTO(
    val id: Long,
    val boardType: String,
    val title: String,
    val content: String,
    val nickname: String,
    val tags: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)