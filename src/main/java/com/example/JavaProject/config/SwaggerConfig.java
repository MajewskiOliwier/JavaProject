package com.example.JavaProject.config;

import com.example.JavaProject.exception.ErrorInfo;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenApiCustomizer globalErrorResponses() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    ApiResponses responses = operation.getResponses();

                    responses.forEach((statusCode, apiResponse) ->
                            apiResponse.getContent().addMediaType("application/json", new MediaType()));

                    responses.addApiResponse("500", new ApiResponse()
                            .description("Internal Server Error - Unexpected server issue")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(new Schema<>().$ref(ErrorInfo.class.getSimpleName()))
                                            .example(new ErrorInfo("/api/users/123", "User not found")))));

                })
        );
    }
}
