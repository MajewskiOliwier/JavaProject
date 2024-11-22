package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.response.LikesCountResponse;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface AccountManagementService {
    String promote(long id);

    RegisterDto updateAccount(RegisterDto registerDto);

    @Transactional
    String deleteAccount();

    Optional<User> getUserByEmail(String email);

    String hideAccount(long id);

}
