# 1. Gradle 빌드 환경 설정
FROM eclipse-temurin:17-jdk as build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. gradlew 및 소스 코드 복사 (gradle 디렉토리 포함)
COPY gradlew .
COPY gradle gradle
COPY . .

# 4. gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 5. Gradle 실행 및 빌드 (테스트 제외 가능)
RUN ./gradlew clean build -x test

# 6. 런타임 환경 설정
FROM eclipse-temurin:17-jre

# 7. 작업 디렉토리 설정
WORKDIR /app

# 8. 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 9. 환경 변수로 application.yml 생성
RUN mkdir -p src/main/resources && \
    echo "spring:" >> src/main/resources/application.yml && \
    echo "  application:" >> src/main/resources/application.yml && \
    echo "    name: raindrop" >> src/main/resources/application.yml && \
    echo "  datasource:" >> src/main/resources/application.yml && \
    echo "    url: ${SPRING_DATASOURCE_URL}" >> src/main/resources/application.yml && \
    echo "    username: ${SPRING_DATASOURCE_USERNAME}" >> src/main/resources/application.yml && \
    echo "    password: ${SPRING_DATASOURCE_PASSWORD}" >> src/main/resources/application.yml && \
    echo "    driver-class-name: com.mysql.cj.jdbc.Driver" >> src/main/resources/application.yml && \
    echo "    hikari:" >> src/main/resources/application.yml && \
    echo "      maximum-pool-size: 10" >> src/main/resources/application.yml && \
    echo "  flyway:" >> src/main/resources/application.yml && \
    echo "    enabled: false" >> src/main/resources/application.yml && \
    echo "    locations: classpath:db/migration" >> src/main/resources/application.yml && \
    echo "    validate-on-migrate: true" >> src/main/resources/application.yml && \
    echo "kakao:" >> src/main/resources/application.yml && \
    echo "  client-id: ${KAKAO_CLIENT_ID}" >> src/main/resources/application.yml && \
    echo "  redirect-uri: http://localhost:5173/auth/login/kakao" >> src/main/resources/application.yml && \
    echo "jasypt:" >> src/main/resources/application.yml && \
    echo "  encryptor:" >> src/main/resources/application.yml && \
    echo "    password: ${JASYPT_ENCRYPTOR_PASSWORD}" >> src/main/resources/application.yml && \
    echo "    algorithm: PBEWithMD5AndDES" >> src/main/resources/application.yml
# 10. 실행 명령 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
