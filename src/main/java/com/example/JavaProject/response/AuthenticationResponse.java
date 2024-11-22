package com.example.JavaProject.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing the access token and token type")
public class AuthenticationResponse {

    @Schema(description = "jwt token returned to user")
    private String accessToken;

    @Schema(description = "type of token", example = "Bearer")
    private String tokenType;
}
