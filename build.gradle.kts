import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import ru.vyarus.gradle.plugin.python.PythonExtension

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    id("org.springframework.boot") version "2.7.10"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.springframework.cloud.contract") version "3.1.6"
    id("au.com.dius.pact") version "4.5.5"
    id("io.qameta.allure-aggregate-report") version "2.11.2"
    id("com.appland.appmap") version "1.1.1"
    id("org.hidetake.swagger.generator") version "2.19.2"
    id("io.github.redgreencoding.plantuml") version "0.2.0"
    id("ru.vyarus.mkdocs") version "3.0.0"
    id("net.saliman.properties") version "1.5.2"
    //id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

sourceSets {
    create("acceptanceTest") {
        kotlin.srcDir("src/acceptanceTest/kotlin")
        resources.srcDir("src/acceptanceTest/resources")
        compileClasspath += sourceSets.main.get().compileClasspath + sourceSets.test.get().compileClasspath
        annotationProcessorPath += sourceSets.test.get().annotationProcessorPath
    }
    create("archTest") {
        kotlin.srcDir("src/archTest/kotlin")
        resources.srcDir("src/archTest/resources")
        compileClasspath += sourceSets.main.get().compileClasspath + sourceSets.test.get().compileClasspath
        annotationProcessorPath += sourceSets.test.get().annotationProcessorPath
    }
    create("c4") {
        kotlin.srcDir("src/c4/kotlin")
        resources.srcDir("src/c4/resources")
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
        annotationProcessorPath += sourceSets.main.get().annotationProcessorPath
    }
}

repositories {
    mavenCentral()
    maven(url= "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

ext {
    set("kotlin-logging.version", "1.12.5")
    set("detekt.version", "1.14.2")
    set("serenity.version", "3.7.1")
    set("mockk.version", "1.11.0")
    set("springmockk.version", "3.0.1")
    set("problem-spring-webflux.version", "0.27.0")
    set("structurizr-kotlin.version", "1.3.0.4")

    set("allure.version", "2.21.0")
    set("liquibase.version", "4.20.0")
    set("atlassian.validator.version", "2.33.1")
    set("cucumber.version", "7.11.2")
    set("spring.cloud.contract.version", "3.1.6")
    set("embedded-database-spring-test.version", "2.2.0")
    set("junit.platform.version", "5.7.1")
    set("structurizr.version", "1.9.4")
    set("structurizr-dsl.version", "1.10.0")
    set("structurizr-annotations.version", "1.3.5")
    set("structurizr-analysis.version", "1.3.5")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("io.github.microutils:kotlin-logging:${property("kotlin-logging.version")}")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.zalando:problem-spring-webflux:0.27.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.atlassian.oai:swagger-request-validator-core:${property("atlassian.validator.version")}")
    implementation("com.atlassian.oai:swagger-request-validator-springmvc:${property("atlassian.validator.version")}")
    implementation("io.micrometer:micrometer-registry-prometheus")

    implementation("org.flywaydb:flyway-core")
    runtimeOnly("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
    implementation("org.postgresql:postgresql")

    // testing
    testImplementation("org.junit.platform:junit-platform-suite")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "junit", module = "junit")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.zonky.test:embedded-database-spring-test:${property("embedded-database-spring-test.version")}")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.mockk:mockk:${property("mockk.version")}")
    testImplementation("com.ninja-squad:springmockk:${property("springmockk.version")}")
    testImplementation("io.qameta.allure:allure-junit5:${property("allure.version")}")
    testImplementation("org.flywaydb:flyway-core")
    testImplementation("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")

    testRuntimeOnly("org.postgresql:postgresql")

    // AT testing
    implementation("io.cucumber:cucumber-java:${property("cucumber.version")}")
    testImplementation("io.cucumber:cucumber-spring:${property("cucumber.version")}")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:${property("cucumber.version")}")
    testImplementation("io.qameta.allure:allure-cucumber7-jvm:${property("allure.version")}")

    // CDC testing
    testImplementation("au.com.dius.pact.consumer:junit5:4.5.5") {
        exclude("org.apache.groovy", "groovy")
    }
    testImplementation("au.com.dius.pact.provider:junit5:4.5.5") {
        exclude("org.apache.groovy", "groovy")
    }
    testImplementation("org.codehaus.groovy:groovy-all:3.0.17")
    testRuntimeOnly("io.zonky.test:embedded-postgres:2.0.4")

    // ARCH testing
    testImplementation("com.tngtech.archunit:archunit-junit5:1.0.1")

    // API documentation
    swaggerUI("org.webjars:swagger-ui:4.1.3")

    // C4 documentation
    implementation("com.structurizr:structurizr-annotations:${property("structurizr-annotations.version")}")
    implementation("com.structurizr:structurizr-analysis:${property("structurizr-analysis.version")}")
    implementation("com.structurizr:structurizr-core:${property("structurizr.version")}")
    implementation("com.structurizr:structurizr-client:${property("structurizr.version")}")
    implementation("com.structurizr:structurizr-dsl:${property("structurizr-dsl.version")}")
    implementation("cc.catalysts.structurizr:structurizr-kotlin:${property("structurizr-kotlin.version")}")

    // source code analysis
    //runtimeOnly("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.1")
    //detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${property("detekt.version")}")
}

/*detekt {
    toolVersion = "${property("detekt.version")}"
    config = files("$projectDir/detekt.yml")
    buildUponDefaultConfig = true
    ignoreFailures = true
    source = files("$projectDir/src/main/kotlin", "$projectDir/src/test/kotlin")
    reportsDir = file("$buildDir/reports/detekt")
}*/

contracts {
    failOnNoContracts.set(false)
}

allure {
}

appmap {
    configFile.set(file("$projectDir/appmap.yml"))
    outputDirectory.set(file("$buildDir/appmap"))
    isSkip = false
    debug = "info"
    debugFile.set(file("$buildDir/appmap/agent.log"))
    eventValueSize = 1024
}

swaggerSources {
    create("apidoc") {
        setInputFile(file("$rootDir/src/main/resources/api/openapi.yaml"))
    }
}

plantuml {
    options {
        format = "svg"
    }
    diagrams {
        create("hexagonal") {
            sourceFile = project.file("doc/uml/hexagonal.puml")
        }
        create("domain-model") {
            sourceFile = project.file("doc/uml/domain-model.puml")
        }
        create("data-model") {
            sourceFile = project.file("doc/uml/data-model.puml")
        }
    }
}

python {
    scope = PythonExtension.Scope.USER
}

mkdocs {
    strict = false
    updateSiteUrl = false
    sourcesDir = "doc"
    buildDir = "build/mkdocs"
    publish.docPath = ""
}

gitPublish {
    contents {
        from(mkdocs.buildDir)
        from("build/reports/tests") {
            into("reports/tests")
        }
        from("build/plantuml") {
            into("uml")
        }
        from("build/swagger-ui-apidoc") {
            into("api")
        }
        from("doc/postman") {
            into("postman")
        }
        from("build/appmap/junit") {
            into("appmap")
        }
    }
}

springBoot {
    mainClass.set("edu.obya.blueprint.client.ApplicationKt")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        }
    }
    contractTest {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        }
    }
    val acceptanceTest by registering(Test::class) {
        description = "Runs the acceptance tests"
        group = "verification"
        testClassesDirs = sourceSets["acceptanceTest"].output.classesDirs
        classpath += sourceSets["acceptanceTest"].runtimeClasspath
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        }
    }
    val archTest by registering(Test::class) {
        description = "Runs the architecture tests"
        group = "verification"
        testClassesDirs = sourceSets["archTest"].output.classesDirs
        classpath += sourceSets["archTest"].runtimeClasspath
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        }
    }
    named<Copy>("processAcceptanceTestResources") {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }
    named<Copy>("processArchTestResources") {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }
    check {
        dependsOn(acceptanceTest)
        dependsOn(archTest)
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all-compatibility")
            jvmTarget = "17"
        }
    }
    /*withType<io.gitlab.arturbosch.detekt.Detekt> {
        jvmTarget = "17"
    }*/
}
