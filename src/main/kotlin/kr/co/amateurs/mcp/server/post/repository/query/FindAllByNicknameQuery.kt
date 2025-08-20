package kr.co.amateurs.mcp.server.post.repository.query

import kr.co.amateurs.mcp.server.common.dto.PaginationSortType
import kr.co.amateurs.mcp.server.post.dto.request.PostSearchParams
import kr.co.amateurs.mcp.server.post.dto.response.PostResponseDTO
import org.jooq.*
import org.jooq.generated.tables.references.POSTS
import org.jooq.generated.tables.references.USERS
import org.jooq.impl.DSL.noCondition
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort

class FindAllByNicknameQuery(
    private val dsl: DSLContext,
    private val params: PostSearchParams,
) : PostQuery<PageImpl<PostResponseDTO>> {

    override fun execute(): PageImpl<PostResponseDTO> {
        val selectQuery = buildSelectQuery()
        val joinQuery = buildJoinQuery(selectQuery)
        val posts = fetchPosts(joinQuery, params)
        val totalCount = getTotalCount(buildConditions(params))

        return PageImpl(posts, params.toPageable(), totalCount.toLong())
    }

    private fun buildSelectQuery(): SelectSelectStep<*> {
        return dsl.select(
            POSTS.ID.`as`("id"),
            POSTS.BOARD_TYPE.`as`("boardType"),
            POSTS.TITLE.`as`("title"),
            POSTS.CONTENT.`as`("content"),
            USERS.NICKNAME.`as`("nickname"),
            POSTS.TAG.`as`("tags"),
            POSTS.CREATED_AT.`as`("createdAt"),
            POSTS.UPDATED_AT.`as`("updatedAt")
        )
    }

    private fun buildJoinQuery(selectQuery: SelectSelectStep<*>): SelectJoinStep<*> {
        return selectQuery.from(POSTS)
            .join(USERS).on(POSTS.USER_ID.eq(USERS.ID))
    }

    private fun fetchPosts(query: SelectJoinStep<*>, params: PostSearchParams): List<PostResponseDTO> {
        val condition = buildConditions(params)
        val orderBy = buildOrderBy(params)

        return query.where(condition)
            .orderBy(orderBy)
            .limit(params.pageSize)
            .offset(params.pageNumber * params.pageSize)
            .fetchInto(PostResponseDTO::class.java)
    }

    private fun buildConditions(params: PostSearchParams): Condition {
        var condition = noCondition()

        params.keyword.let { keyword ->
            condition = condition
                .or(POSTS.users.NICKNAME.eq(keyword))
        }

        return condition
    }

    private fun buildOrderBy(params: PostSearchParams): OrderField<*> {
        fun Field<*>.orderBy(direction: Sort.Direction) = if (direction.isAscending) this.asc() else this.desc()

        return when (params.field) {
            PaginationSortType.CREATED_AT -> POSTS.CREATED_AT.orderBy(params.direction)
            PaginationSortType.UPDATED_AT -> POSTS.UPDATED_AT.orderBy(params.direction)
            else -> POSTS.ID.orderBy(params.direction)
        }
    }

    private fun getTotalCount(condition: Condition): Int {
        return dsl.selectCount()
            .from(POSTS)
            .join(USERS).on(POSTS.USER_ID.eq(USERS.ID))
            .where(condition)
            .fetchOptional(0, Int::class.java)
            .orElse(0)
    }
}