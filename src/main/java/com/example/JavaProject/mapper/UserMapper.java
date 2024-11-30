package com.example.JavaProject.mapper;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

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
