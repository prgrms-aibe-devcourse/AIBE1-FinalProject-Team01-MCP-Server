package kr.co.amateurs.mcp.server.post.dto.response

import org.jooq.generated.enums.PostsBoardType
import java.time.LocalDateTime

data class PostResponseDTO(
    val id: Long,
    val boardType: PostsBoardType,
    val title: String,
    val content: String,
    val nickname: String,
    val tags: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)