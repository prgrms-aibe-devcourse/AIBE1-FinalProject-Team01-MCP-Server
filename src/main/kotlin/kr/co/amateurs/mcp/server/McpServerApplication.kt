package kr.co.amateurs.mcp.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class McpServerApplication

fun main(args: Array<String>) {
    runApplication<McpServerApplication>(*args)
}
