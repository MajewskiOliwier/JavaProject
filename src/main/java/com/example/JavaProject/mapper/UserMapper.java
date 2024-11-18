package com.example.JavaProject.mapper;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.RecipeIngredient;
import com.example.JavaProject.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public User mapToEntity(RegisterDto registerDto){
        return User.builder()
                .userName(registerDto.getUsername())
                .age(registerDto.getAge())
                .isMan(registerDto.getIsMan())
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .build();
    }
    public RegisterDto mapToDto(User user){
        return new RegisterDto(
                user.getNormalUsername(),
                user.getAge(),
                user.getIsMan(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
