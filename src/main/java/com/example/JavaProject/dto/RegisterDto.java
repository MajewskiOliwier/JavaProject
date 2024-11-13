package com.example.JavaProject.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {


    @NotBlank(message = "Username cannot be empty")
    @Size(min = 1, max = 50, message = "Username length must be between 1 and 50 characters")
    private String username;

    @NotNull(message = "Age must be provided")
    @Min(value = 6, message = "Age must be at least 6")
    @Max(value = 99, message = "Age cannot be greater than 99")
    private Integer age;

    @NotNull(message = "Gender must be specified")
    private Boolean isMan;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
