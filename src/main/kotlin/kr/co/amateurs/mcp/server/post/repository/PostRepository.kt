package kr.co.amateurs.mcp.server.post.repository

import kr.co.amateurs.mcp.server.post.dto.request.PostSearchParams
import kr.co.amateurs.mcp.server.post.dto.response.PostResponseDTO
import kr.co.amateurs.mcp.server.post.repository.query.FindAllByNicknameQuery
import kr.co.amateurs.mcp.server.post.repository.query.FindAllQuery
import kr.co.amateurs.mcp.server.post.repository.query.PostQueryFactory
import org.jooq.DSLContext
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
class PostRepository(
    private val dsl: DSLContext,
    private val postQueryFactory: PostQueryFactory
) {
    fun findAll(params: PostSearchParams): Page<PostResponseDTO> =
        postQueryFactory.buildQuery(FindAllQuery(dsl, params))

    fun findAllByNickname(params: PostSearchParams): Page<PostResponseDTO> =
        postQueryFactory.buildQuery(FindAllByNicknameQuery(dsl, params))
}
