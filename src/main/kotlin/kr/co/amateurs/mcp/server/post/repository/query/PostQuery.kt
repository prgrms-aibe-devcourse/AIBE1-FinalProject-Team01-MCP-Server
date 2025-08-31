package kr.co.amateurs.mcp.server.post.repository.query

interface PostQuery<T> {
    fun execute(): T
}