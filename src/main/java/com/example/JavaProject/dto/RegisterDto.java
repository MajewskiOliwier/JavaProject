package com.example.JavaProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {


    @NotBlank
    @Size(min = 1,max = 40, message = "Username length must be in range <1,40>")
    private String username;


    @NotBlank
    @Email(message = "email must contain character @")
    private String email;

    @NotBlank
    private String password;

}
