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

    @NotBlank
    @Size(min = 1,max = 40, message = "Username length must be in range <1,40>")
    private String username;

    @NotBlank
    @Min(1)
    @Max(120)
    private int age;

    @NotBlank
    private boolean isMan;

    @NotBlank
    @Email(message = "email must contain character @")
    private String email;

    @NotBlank
    @Min(8)
    private String password;

    public Boolean getIsMan() {
        return isMan;
    }

    public void setIsMan(Boolean isMan) {
        this.isMan = isMan;
    }
}
