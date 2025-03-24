# 1. Gradle 빌드 환경 설정
FROM eclipse-temurin:17-jdk as build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. gradlew 및 소스 코드 복사 (gradle 디렉토리 포함)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src


# 4. gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 5. Gradle 실행 및 빌드 (테스트 제외 가능)
RUN ./gradlew clean build -x test

# 6. 런타임 환경 설정
FROM eclipse-temurin:17-jre

# 7. 작업 디렉토리 설정
WORKDIR /app

# 8. 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# 10. 실행 명령 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
