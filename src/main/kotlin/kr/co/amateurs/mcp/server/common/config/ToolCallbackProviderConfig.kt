package kr.co.amateurs.mcp.server.common.config

import kr.co.amateurs.mcp.server.post.service.PostService
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ToolCallbackProviderConfig (
    private val postService: PostService
) {
    @Bean
    fun toolCallbackProvider(): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder()
            .toolObjects(postService)
            .build()
    }
}