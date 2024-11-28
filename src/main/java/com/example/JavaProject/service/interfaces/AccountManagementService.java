package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RegisterDto;

public interface AccountManagementService {
    String promote(long id);

    RegisterDto updateAccount(RegisterDto registerDto);

    String deleteAccount();

    String getInfoByEmail(String email);

    String hideAccount(long id, boolean hidden);
}
