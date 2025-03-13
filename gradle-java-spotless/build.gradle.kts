plugins {
    id("com.diffplug.spotless") version "6.25.0"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
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
