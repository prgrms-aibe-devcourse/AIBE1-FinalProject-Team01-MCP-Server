package kr.co.amateurs.mcp.server.post.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.co.amateurs.mcp.server.post.dto.request.PostSearchParams
import org.jooq.DSLContext
import org.jooq.generated.enums.PostsBoardType
import org.jooq.generated.enums.UsersDevcourseName
import org.jooq.generated.enums.UsersRole
import org.jooq.generated.tables.references.POSTS
import org.jooq.generated.tables.references.USERS
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostServiceTest(
    private val postService: PostService,
    private val dslContext: DSLContext
) : BehaviorSpec({

    beforeEach {
        dslContext.insertInto(USERS)
            .set(USERS.ID, 1L)
            .set(USERS.EMAIL, "test1@example.com")
            .set(USERS.NAME, "테스트사용자1")
            .set(USERS.NICKNAME, "testuser1")
            .set(USERS.ROLE, UsersRole.STUDENT)
            .set(USERS.DEVCOURSE_BATCH, "1")
            .set(USERS.DEVCOURSE_NAME, UsersDevcourseName.AI_BACKEND)
            .set(USERS.IS_PROFILE_COMPLETED, true)
            .set(USERS.IS_DELETED, false)
            .execute()

        dslContext.insertInto(USERS)
            .set(USERS.ID, 2L)
            .set(USERS.EMAIL, "test2@example.com")
            .set(USERS.NAME, "테스트사용자2")
            .set(USERS.NICKNAME, "testuser2")
            .set(USERS.ROLE, UsersRole.STUDENT)
            .set(USERS.DEVCOURSE_BATCH, "1")
            .set(USERS.DEVCOURSE_NAME, UsersDevcourseName.FRONTEND)
            .set(USERS.IS_PROFILE_COMPLETED, true)
            .set(USERS.IS_DELETED, false)
            .execute()

        // 테스트용 게시글 데이터 생성
        dslContext.insertInto(POSTS)
            .set(POSTS.TITLE, "Spring Boot 개발 가이드")
            .set(POSTS.CONTENT, "Spring Boot로 REST API를 개발하는 방법에 대해 설명합니다.")
            .set(POSTS.BOARD_TYPE, PostsBoardType.QNA)
            .set(POSTS.USER_ID, 1L)
            .set(POSTS.LIKE_COUNT, 5)
            .set(POSTS.IS_BLINDED, false)
            .set(POSTS.IS_DELETED, false)
            .set(POSTS.CREATED_AT, LocalDateTime.now())
            .set(POSTS.UPDATED_AT, LocalDateTime.now())
            .execute()

        dslContext.insertInto(POSTS)
            .set(POSTS.TITLE, "Kotlin 코루틴 활용법")
            .set(POSTS.CONTENT, "Kotlin의 코루틴을 사용하여 비동기 프로그래밍을 구현하는 방법입니다.")
            .set(POSTS.BOARD_TYPE, PostsBoardType.INFO)
            .set(POSTS.USER_ID, 2L)
            .set(POSTS.LIKE_COUNT, 3)
            .set(POSTS.IS_BLINDED, false)
            .set(POSTS.IS_DELETED, false)
            .set(POSTS.CREATED_AT, LocalDateTime.now())
            .set(POSTS.UPDATED_AT, LocalDateTime.now())
            .execute()
    }

    afterEach {
        // 테스트 데이터 정리
        dslContext.deleteFrom(POSTS).execute()
        dslContext.deleteFrom(USERS).execute()
    }

    Context("게시글 조회 관련 툴") {
        Given("유효한 키워드가 주어졌을 때") {
            When("search_posts 툴을 실행하면") {
                Then("키워드와 일치하는 게시글 목록을 반환해야 한다") {
                    val params = PostSearchParams(
                        keyword = "Spring",
                        pageNumber = 0,
                        pageSize = 10
                    )

                    val result = postService.searchPosts(params)

                    result shouldNotBe null
                    result.content.size shouldBe 1
                    result.content[0].title shouldBe "Spring Boot 개발 가이드"
                    result.content[0].nickname shouldBe "testuser1"
                    result.totalElements shouldBe 1
                }
            }

            When("search_posts_by_nickname 툴을 실행하면") {
                Then("작성자 닉네임과 일치하는 게시글 목록을 반환해야 한다") {
                    val params = PostSearchParams(
                        keyword = "testuser2",
                        pageNumber = 0,
                        pageSize = 10
                    )

                    val result = postService.searchByNickname(params)

                    result shouldNotBe null
                    result.content.size shouldBe 1
                    result.content[0].title shouldBe "Kotlin 코루틴 활용법"
                    result.content[0].nickname shouldBe "testuser2"
                    result.totalElements shouldBe 1
                }
            }
        }

        Given("유효하지 않은 키워드가 주어졌을 때") {
            When("search_posts 툴을 실행하면") {
                Then("예외가 발생해야 한다") {
                    val params = PostSearchParams(
                        keyword = "",
                        pageNumber = 0,
                        pageSize = 10
                    )

                    shouldThrow<Exception> {
                        postService.searchPosts(params)
                    }
                }
            }

            When("search_posts_by_nickname 툴을 실행하면") {
                Then("예외가 발생해야 한다") {
                    val params = PostSearchParams(
                        keyword = "",
                        pageNumber = 0,
                        pageSize = 10
                    )

                    shouldThrow<Exception> {
                        postService.searchPosts(params)
                    }
                }
            }
        }
    }
})