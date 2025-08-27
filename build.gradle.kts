import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Generate

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("dev.monosoul.jooq-docker") version "6.1.9"
    id("jacoco")
}

group = "kr.co.amateurs"
version = "0.0.1-SNAPSHOT"
val jooqVersion = "3.19.15"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

extra["jooqVersion"] = "3.19.15"
extra["springAiVersion"] = "1.0.0"

sourceSets {
    main {
        kotlin {
            setSrcDirs(listOf("src/main/kotlin", "src/generated"))
        }
    }
}

jooq {
    version = jooqVersion

    withContainer {
        image {
            name = "mysql:8.0.41"
            envVars = mapOf(
                "MYSQL_ROOT_PASSWORD" to "passwd",
                "MYSQL_DATABASE" to "amateurs"
            )
        }

        db {
            username = "root"
            password = "passwd"
            name = "amateurs"
            port = 3306
            jdbc {
                schema = "jdbc:mysql"
                driverClassName = "com.mysql.cj.jdbc.Driver"
            }
        }
    }
}

jacoco {
    toolVersion = "0.8.13"
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required = true
        html.required = true
        csv.required = true
    }

    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/generated/**",

                    "**/config/**",
                    "**/domain/**",
                    "**/exception/**",
                    "**/handler/**",
                    "**/repository/**",
                    "**/utils/**",

                    "**/*Application.*",
                )
            }
        }))
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)

    violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.50".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.50".toBigDecimal()
            }
        }

        rule {
            element = "CLASS"
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.50".toBigDecimal()
            }

            excludes = listOf(
                "*.Application",
                "*.Config*",
                "*.Configuration*"
            )
        }
    }
}

tasks {
    generateJooqClasses {
        schemas = listOf("amateurs")
        outputDirectory = project.layout.projectDirectory.dir("src/generated")
        includeFlywayTable = false

        usingJavaConfig {
            withName("org.jooq.codegen.KotlinGenerator")

            generate = Generate()
                .withJavaTimeTypes(true)
                .withDeprecated(false)
                .withDaos(false)
                .withFluentSetters(true)
                .withRecords(true)
                .withPojos(true)
                .withSpringAnnotations(true)
                .withValidationAnnotations(true)
                .withKotlinNotNullRecordAttributes(true)

            database.withForcedTypes(
                ForcedType()
                    .withUserType("java.lang.Long")
                    .withTypes("int unsigned"),
                ForcedType()
                    .withUserType("java.lang.Integer")
                    .withTypes("tinyint unsigned"),
                ForcedType()
                    .withUserType("java.lang.Integer")
                    .withTypes("smallint unsigned"),
                ForcedType()
                    .withUserType("java.lang.Boolean")
                    .withTypes("tinyint\\(1\\)")
            )
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.ai:spring-ai-starter-mcp-server")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
    testImplementation("io.kotest:kotest-property:5.8.1")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.11.0"))
    implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
    implementation("io.micrometer:micrometer-registry-otlp")

    implementation("org.springframework.boot:spring-boot-starter-jooq") {
        exclude(group = "org.jooq")
    }
    implementation("org.jooq:jooq:$jooqVersion")

    jooqCodegen("org.jooq:jooq:$jooqVersion")
    jooqCodegen("org.jooq:jooq-meta:$jooqVersion")
    jooqCodegen("org.jooq:jooq-codegen:$jooqVersion")
    jooqCodegen("com.mysql:mysql-connector-j")
    jooqCodegen("org.flywaydb:flyway-core")
    jooqCodegen("org.flywaydb:flyway-mysql")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.compileKotlin {
    dependsOn("generateJooqClasses")
}

tasks.clean {
    delete("src/generated")
}