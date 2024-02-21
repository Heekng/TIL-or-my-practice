plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.diffplug.spotless") version "6.25.0"
}

group = "com.heekng"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

spotless {
    java {
        googleJavaFormat()

        importOrder() // import 정렬
        removeUnusedImports() // 사용하지 않는 import 제거
        trimTrailingWhitespace() // 공백 제거
        indentWithTabs(2)
        indentWithSpaces(4)
        endWithNewline() // 마지막 라인에 NewLine
    }
}
