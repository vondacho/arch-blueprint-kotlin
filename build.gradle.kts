plugins {
    kotlin("jvm").version("1.5.0")
    kotlin("plugin.spring").version("1.5.0")
    id("org.springframework.boot").version("2.5.0")
    id("io.spring.dependency-management").version("1.0.11.RELEASE")

    id("com.google.cloud.tools.jib").version("3.0.0")
    id("io.gitlab.arturbosch.detekt").version("1.14.2")
    id("com.appland.appmap").version("1.0.1")

    // waiting for https://github.com/serenity-bdd/serenity-gradle-plugin/pull/4
    id("net.serenity-bdd").version("2.4.24")
    // waiting for https://github.com/allure-framework/allure-gradle/pull/61
    id("io.qameta.allure").version("2.9")
}

repositories {
    mavenLocal()
    mavenCentral()
    // Dependency: Detekt:1.14.2 -> org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2
    maven(url= "https://dl.bintray.com/kotlin/kotlinx/")
}

ext {
    set("kotlin-logging.version", "1.12.5")
    set("detekt.version", "1.14.2")
    set("cucumber.version", "6.10.3")
    set("serenity.version", "2.4.24")
    set("allure.version", "2.13.9")
    set("mockk.version", "1.11.0")
    set("springmockk.version", "3.0.1")
    set("junit.platform.version", "5.7.1")
    set("embedded-postgresql.version", "2.0.8")
    set("atlassian.validator.version", "2.18.0")
    set("problem-spring-webflux.version", "0.26.2")
    set("structurizr.version", "1.9.4")
    set("structurizr-dsl.version", "1.10.0")
    set("structurizr-annotations.version", "1.3.5")
    set("structurizr-analysis.version", "1.3.5")
    set("structurizr-kotlin.version", "1.2.0")
}

dependencies {
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    // reactor
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    // logging
    implementation("io.github.microutils:kotlin-logging:${property("kotlin-logging.version")}")
    // spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    // problems
    implementation("org.zalando:problem-spring-webflux:${property("problem-spring-webflux.version")}")
    // serialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // persistence
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    // database
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    // management
    implementation("org.springframework.boot:spring-boot-actuator")
    implementation("org.springframework.boot:spring-boot-actuator-autoconfigure")
    // metrics
    implementation("io.micrometer:micrometer-registry-prometheus")
    // openapi
    implementation("com.atlassian.oai:swagger-request-validator-core:${property("atlassian.validator.version")}")

    // test/spring
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.0.2")
    // test/engine
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junit.platform.version")}")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:${property("junit.platform.version")}")
    // test/reactor
    testImplementation("io.projectreactor:reactor-test")
    // test/mock
    testImplementation("io.mockk:mockk:${property("mockk.version")}")
    testImplementation("com.ninja-squad:springmockk:${property("springmockk.version")}")
    // test/bdd
    testImplementation("io.cucumber:cucumber-junit:${property("cucumber.version")}")
    testImplementation("io.cucumber:cucumber-java:${property("cucumber.version")}")
    testImplementation("io.cucumber:cucumber-java8:${property("cucumber.version")}")
    testImplementation("io.cucumber:cucumber-spring:${property("cucumber.version")}")
    // test/reporting
    testImplementation("net.serenity-bdd:serenity-cucumber6:${property("serenity.version")}")
    testImplementation("io.qameta.allure:allure-junit5:${property("allure.version")}")
    implementation("io.qameta.allure:allure-cucumber6-jvm:${property("allure.version")}")
    // test/containers
    testImplementation("com.playtika.testcontainers:embedded-postgresql:${property("embedded-postgresql.version")}")
    // test/database
    testImplementation("org.flywaydb:flyway-core")
    testImplementation("io.r2dbc:r2dbc-postgresql")
    testImplementation("org.postgresql:postgresql")

    // c4 structurizr
    implementation("com.structurizr:structurizr-annotations:${property("structurizr-annotations.version")}")
    implementation("com.structurizr:structurizr-analysis:${property("structurizr-analysis.version")}")
    implementation("com.structurizr:structurizr-core:${property("structurizr.version")}")
    implementation("com.structurizr:structurizr-client:${property("structurizr.version")}")
    implementation("com.structurizr:structurizr-dsl:${property("structurizr-dsl.version")}")
    implementation("cc.catalysts.structurizr:structurizr-kotlin:${property("structurizr-kotlin.version")}")

    // application flows recording
    implementation(files("libs/appmap-1.1.0.jar"))

    // source code analysis
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${property("detekt.version")}")
}

version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11

springBoot {
    mainClass.set("edu.software.craftsmanship.blueprint.boot.BlueprintApplicationKt")
    buildInfo {
        properties {
            artifact = "arch-blueprint-kotlin"
            version = System.getProperty("version") ?: "unspecified"
            group = "edu.software.craftsmanship"
            name = "blueprint"
        }
    }
}

configure<ProcessResources>("processResources") {
    filesMatching("**/*.yml") {
        expand(project.properties)
    }
    from("openapi.yaml") {
        into(".")
    }
}

inline fun <reified C> Project.configure(name: String, configuration: C.() -> Unit) {
    (this.tasks.getByName(name) as C).configuration()
}

jib {
    container {
        jvmFlags = mutableListOf(
            "-XX:+UseContainerSupport",
            "-XX:MaxRAMPercentage=50.0",
            "-Djava.security.egd=file:/dev/urandom",
            "-Dsun.net.inetaddr.ttl=0",
            "-Dsun.net.inetaddr.negative.ttl=0"
        )
    }
}

detekt {
    toolVersion = "${property("detekt.version")}"
    input = files("$projectDir/src/main/kotlin", "$projectDir/src/test/kotlin")
    config = files("$projectDir/detekt.yml")
    buildUponDefaultConfig = true
    ignoreFailures = true

    reports {
        xml {
            enabled = true
            destination = file("$buildDir/reports/detekt/detekt.xml")
        }
        html {
            enabled = true
            destination = file("$buildDir/reports/detekt/detekt.html")
        }
    }
}

allure {
    version = "2.13.9"
    aspectjweaver = true
    autoconfigure = true
    resultsDir = file("$buildDir/reports/tests/allure-results")
    reportDir = file("$buildDir/reports/tests/allure")
}

appmap {
    configFile.set(file("$projectDir/appmap.yml"))
    outputDirectory.set(file("$buildDir/appmap"))
    debug = "info"
    debugFile.set(file("$buildDir/appmap/agent.log"))
    eventValueSize = 1024
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all-compatibility")
            jvmTarget = "11"
        }
    }

    withType<io.gitlab.arturbosch.detekt.Detekt> {
        jvmTarget = "11"
    }

    withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        environment["spring.profiles.active"] = "local"
        environment["spring.output.ansi.console-available"] = true
    }
}
