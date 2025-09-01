package kr.co.amateurs.mcp.server.common.config

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ToolCallbackProviderConfigTest(
    toolCallbackProvider: ToolCallbackProvider
) : BehaviorSpec({

    Given("ToolCallbackProvider의 도구 정의들을 검증할 때") {
        val toolCallBacks = toolCallbackProvider.toolCallbacks

        When("각 도구의 정의를 확인하면") {
            Then("search_posts 툴이 올바르게 정의되어야 한다") {
                val searchPostsTool = toolCallBacks.find {
                    it.toolDefinition.name() == "search_posts"
                }
                searchPostsTool shouldNotBe null
            }

            Then("search_posts_by_nickname 툴이 올바르게 정의되어야 한다") {
                val searchPostsByNicknameTool = toolCallBacks.find {
                    it.toolDefinition.name() == "search_posts_by_nickname"
                }
                searchPostsByNicknameTool shouldNotBe null
            }
        }
    }

})
