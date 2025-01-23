# 빌드 스테이지
FROM openjdk:17-alpine AS builder
WORKDIR /app
COPY gradle gradle
COPY gradle/build.gradle .
COPY gradle/settings.gradle .
COPY gradlew .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# 실행 스테이지
FROM openjdk:17-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

