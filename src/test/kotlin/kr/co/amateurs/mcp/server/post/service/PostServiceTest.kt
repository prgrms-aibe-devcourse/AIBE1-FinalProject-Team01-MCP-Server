package kr.co.amateurs.mcp.server.post.service

import kr.co.amateurs.mcp.server.post.repository.PostRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostServiceTest(
    private val postRepository: PostRepository
) {
    @BeforeEach
    fun setUp() {
        TODO("Not yet implemented")
    }

    @AfterEach
    fun tearDown() {
        TODO("Not yet implemented")
    }

    @Test
    fun 키워드로_게시글을_검색할_수_있어야_한다() {
        // given

        // when

        // then
    }

    @Test
    fun 키워드없이_게시글_검색을_요청하면_예외가_발생해야_한다() {
        // given

        // when

        // then
    }

    @Test
    fun 닉네임으로_게시글을_검색할_수_있어야_한다() {
        // given

        // when

        // then
    }

    @Test
    fun 닉네임없이_게시글_검색을_요청하면_예외가_발생해야_한다() {
        // given

        // when

        // then
    }
}