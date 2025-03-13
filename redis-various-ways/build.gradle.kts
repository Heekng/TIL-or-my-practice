dependencies {
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation("com.redis.testcontainers:testcontainers-redis-junit:1.6.4")
}
