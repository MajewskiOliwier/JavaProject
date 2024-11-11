package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.LoginDto;
import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.response.AuthenticationResponse;

public interface AuthenticationService {

    String register(RegisterDto registerDto);

    AuthenticationResponse login(LoginDto loginDto);

    Long getCurrentUserId();
}
