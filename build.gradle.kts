import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val queryDslVersion = "5.0.0"

plugins {
    kotlin("kapt") version "1.5.30"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.aps"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // rabbitMQ
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0")

    // queryDsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    // mongo
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb") {
        exclude(group = "org.mongodb", module = "mongo-java-driver")  // dsl과 spring이 둘다 몽고
        // 드라이버를 가지고 있으므로 한쪽 제외
    }
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.2.1") {
        exclude(group = "org.mongodb", module = "mongo-java-driver")
    }

    // gson
    implementation("com.google.code.gson:gson:2.8.9")

    //email
    implementation("org.springframework.boot:spring-boot-starter-mail")

    //expiringMap
    implementation("net.jodah:expiringmap:0.5.9")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
