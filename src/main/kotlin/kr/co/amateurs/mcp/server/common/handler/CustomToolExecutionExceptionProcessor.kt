package kr.co.amateurs.mcp.server.common.handler

import jakarta.persistence.NoResultException
import org.jooq.exception.DataAccessException
import org.jooq.exception.SQLDialectNotSupportedException
import org.springframework.ai.tool.execution.ToolExecutionException
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor
import org.springframework.stereotype.Component
import javax.validation.ValidationException

@Component
class CustomToolExecutionExceptionProcessor: ToolExecutionExceptionProcessor {
    override fun process(exception: ToolExecutionException): String {
        return when (exception.cause) {
            is IllegalArgumentException -> {
                "잘못된 입력값이 제공되었습니다."
            }
            is ValidationException -> {
                "입력값 검증에 실패했습니다."
            }
            is DataAccessException -> {
                "데이터베이스 접근 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
            }
            is SQLDialectNotSupportedException -> {
                "지원하지 않는 데이터베이스 연산입니다."
            }
            is NoResultException -> {
                "검색 조건에 맞는 결과가 없습니다."
            }
            is RuntimeException -> {
                "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
            }
            else -> {
                "알 수 없는 오류가 발생했습니다. 시스템 관리자에게 문의해주세요."
            }
        }
    }
}