package com.example.JavaProject.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Schema(description = "Details about the error response")
public final class ErrorInfo {

    @Schema(description = "Error message")
    private final String message;

    @Schema(description = "Timestamp of the error")
    private final String timestamp;

    @Schema(description = "URL of the request")
    private final String url;

    public ErrorInfo(String url, Exception ex){
        this.message = ex.getMessage();
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.url = url;
    }

    public ErrorInfo(String url, String message){
        this.message = message;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.url = url;
    }
}
