package kr.co.amateurs.mcp.server.post.service

import kr.co.amateurs.mcp.server.post.dto.request.PostSearchParams
import kr.co.amateurs.mcp.server.post.repository.PostRepository
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository
) {
    @Tool(
        name = "search_posts", description = """
        
    """
    )
    fun searchPosts(
        @ToolParam
        postSearchParams: PostSearchParams
    ) = postRepository.findAll(postSearchParams)

    @Tool(
        name = "search_posts_by_nickname", description = """
        
    """
    )
    fun searchByNickname(
        nickname: String
    ) {

    }
}