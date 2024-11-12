package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.response.LikesCountResponse;
import jakarta.transaction.Transactional;

public interface AccountManagementService {
    String promote(long id);

    RegisterDto updateAccount(RegisterDto registerDto);

    @Transactional
    String deleteAccount();
}
