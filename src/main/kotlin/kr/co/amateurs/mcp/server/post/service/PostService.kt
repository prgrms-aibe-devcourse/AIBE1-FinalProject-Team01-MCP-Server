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
        키워드와 일치하는 게시글 목록을 검색합니다.
    """
    )
    fun searchPosts(
        @ToolParam(
            description = """
                keyword: 검색할 키워드
            """
        )
        params: PostSearchParams
    ) = postRepository.findAll(params)

    @Tool(
        name = "search_posts_by_nickname", description = """
        유저의 닉네임과 일치하는 게시글 목록을 검색합니다.
    """
    )
    fun searchByNickname(
        @ToolParam(
            description = """
                keyword: 유저 닉네임
            """
        )
        params: PostSearchParams
    ) = postRepository.findAllByNickname(params)
}