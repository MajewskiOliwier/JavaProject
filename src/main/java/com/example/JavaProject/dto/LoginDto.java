package com.example.JavaProject.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotBlank
    @Email(message = "email must contain character @")
    private String email;

    @NotBlank
    @Min(1)
    @Max(120)
    private int age;

    @NotBlank
    @Min(8)
    private String password;
}
