dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    implementation("org.apache.httpcomponents.client5:httpclient5")

    implementation(platform("software.amazon.awssdk:bom:2.29.39"))
    implementation("software.amazon.awssdk:s3")

    implementation("org.testcontainers:localstack")
}
