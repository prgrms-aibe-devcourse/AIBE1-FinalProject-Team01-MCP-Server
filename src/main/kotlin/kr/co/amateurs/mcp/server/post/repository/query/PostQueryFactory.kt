package kr.co.amateurs.mcp.server.post.repository.query

import org.springframework.stereotype.Component

@Component
class PostQueryFactory {
    fun <T> buildQuery(
        query: PostQuery<T>
    ) = query.execute()
}