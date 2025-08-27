# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Building and Running
- `./gradlew build` - Build the entire project
- `./gradlew bootRun` - Run the Spring Boot application locally
- `./gradlew clean` - Clean build artifacts (also removes generated JOOQ classes)
- `./gradlew generateJooqClasses` - Generate JOOQ database classes from schema

### Testing
- `./gradlew test` - Run all tests
- `./gradlew jacocoTestReport` - Generate test coverage report
- `./gradlew jacocoTestCoverageVerification` - Verify test coverage meets minimum thresholds (50%)

### Code Quality
- Test coverage reports are generated in `build/reports/jacoco/`
- Coverage excludes generated code, config classes, and application entry points
- Minimum coverage requirement: 50% instruction and branch coverage

## Project Architecture

### MCP Server Structure
This is a **Spring AI Model Context Protocol (MCP) Server** that exposes AI tools for post search functionality. The MCP server allows AI models to search through posts using natural language.

### Key Components

**Spring AI Integration:**
- `@Tool` annotated methods in services are automatically exposed as AI tools
- `ToolCallbackProviderConfig` registers service methods as callable tools
- Tools accept structured parameters and return paginated results

**Database Layer:**
- **JOOQ** for type-safe SQL queries (not JPA/Hibernate)
- Generated classes in `src/generated/` from MySQL schema
- Custom query patterns using `PostQueryFactory` and query classes
- MySQL in production, H2 in-memory for tests

**Service Layer:**
- Tools are implemented as `@Tool` annotated methods in service classes
- Current tools: `search_posts`, `search_posts_by_nickname`
- Services receive validated DTOs and return paginated responses

### Data Flow
1. AI model calls MCP tool (e.g., "search_posts")
2. Spring AI routes to `@Tool` annotated method
3. Method receives validated `PostSearchParams`
4. Service delegates to repository with JOOQ queries
5. Returns paginated `PostResponseDTO` results

### Testing Patterns
- Use `@SpringBootTest` and `@ActiveProfiles("test")` for integration tests
- H2 in-memory database for test isolation
- Kotest and JUnit 5 available for assertions
- Test methods use descriptive Korean names (e.g., `키워드로_게시글을_검색할_수_있어야_한다`)

### Configuration Profiles
- `local`: Default profile for development
- `test`: H2 database, disabled Flyway migrations
- `prod`: Production configuration (not in repository)

### Package Structure
```
kr.co.amateurs.mcp.server
├── common/              # Shared DTOs and configurations
│   ├── config/         # Spring configurations
│   └── dto/            # Common data transfer objects
└── post/               # Post-related functionality
    ├── dto/            # Request/response DTOs
    ├── repository/     # JOOQ repositories and queries
    └── service/        # AI tool implementations
```

### Database Schema
- Multi-table design with `posts`, `users`, `user_topics`
- Posts have `board_type` enum for categorization
- Soft delete patterns with `is_deleted`, `is_blinded` flags
- Optimized indexes for common query patterns

### Important Notes
- JOOQ classes are generated from live MySQL schema, not entity classes
- All database access should use JOOQ DSL, not native SQL
- MCP tools must validate input parameters and handle errors gracefully
- Pagination is required for all search operations to prevent large result sets