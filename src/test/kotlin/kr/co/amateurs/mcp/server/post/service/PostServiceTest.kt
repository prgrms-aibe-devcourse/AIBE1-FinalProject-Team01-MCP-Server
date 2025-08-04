package kr.co.amateurs.mcp.server.post.service

import kr.co.amateurs.mcp.server.post.dto.request.PostSearchParams
import kr.co.amateurs.mcp.server.post.repository.PostRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostServiceTest {

    @Autowired
    lateinit var postRepository: PostRepository

    @Test
    fun 키워드로_게시글을_검색할_수_있어야_한다() {
        // given
        val params = PostSearchParams(
            keyword = "test",
        )

        // when
        val posts = postRepository.findAll(params)

        // then
        assertEquals(1, posts.content.size)
    }
}