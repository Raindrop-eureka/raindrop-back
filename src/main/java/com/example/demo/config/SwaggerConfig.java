package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        // ApiResponse 스키마 정의
                        .addSchemas("ApiResponse", createApiResponseSchema())
                        // 공통 에러 응답 정의
                        .addSchemas("BadRequestError", createErrorSchema("잘못된 요청입니다."))
                        .addSchemas("UnauthorizedError", createErrorSchema("인증이 필요합니다."))
                        .addSchemas("ForbiddenError", createErrorSchema("권한이 없습니다."))
                        .addSchemas("NotFoundError", createErrorSchema("리소스를 찾을 수 없습니다."))
                        .addSchemas("ServerError", createErrorSchema("서버 오류가 발생했습니다."))

                        // 공통 응답 컴포넌트 추가
                        .addResponses("BadRequest", createErrorResponse("BadRequestError"))
                        .addResponses("Unauthorized", createErrorResponse("UnauthorizedError"))
                        .addResponses("Forbidden", createErrorResponse("ForbiddenError"))
                        .addResponses("NotFound", createErrorResponse("NotFoundError"))
                        .addResponses("ServerError", createErrorResponse("ServerError")))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Raindrop API") //API제목
                .description("유레카 2기 미니프로제트 빗속말 API문서입니다") //API설명
                .version("1.0"); //api 버전
    }

    // ApiResponse 스키마 생성
    private Schema createApiResponseSchema() {
        Schema apiResponseSchema = new Schema<Object>()
                .type("object")
                .description("API 표준 응답 형식");

        apiResponseSchema.addProperty("success", new Schema<Boolean>().type("boolean").description("요청 성공 여부"));
        apiResponseSchema.addProperty("message", new Schema<String>().type("string").description("응답 메시지"));
        apiResponseSchema.addProperty("data", new Schema<Object>().type("object").description("응답 데이터").nullable(true));
        apiResponseSchema.addProperty("timestamp", new Schema<String>().type("string").format("date-time").description("응답 시간"));

        return apiResponseSchema;
    }

    // 에러 스키마 생성
    private Schema createErrorSchema(String description) {
        Schema errorSchema = new Schema<Object>()
                .type("object")
                .description(description);

        errorSchema.addProperty("success", new Schema<Boolean>().type("boolean").example(false));
        errorSchema.addProperty("message", new Schema<String>().type("string").example(description));
        errorSchema.addProperty("data", new Schema<Object>().type("null").nullable(true));
        errorSchema.addProperty("timestamp", new Schema<String>().type("string").format("date-time"));

        return errorSchema;
    }

    // 에러 응답 생성 - 전체 패키지 경로 사용
    private io.swagger.v3.oas.models.responses.ApiResponse createErrorResponse(String schemaName) {
        return new io.swagger.v3.oas.models.responses.ApiResponse()
                .description("에러 응답")
                .content(
                        new io.swagger.v3.oas.models.media.Content()
                                .addMediaType("application/json",
                                        new io.swagger.v3.oas.models.media.MediaType()
                                                .schema(new Schema<>().$ref(schemaName)))
                );
    }
}