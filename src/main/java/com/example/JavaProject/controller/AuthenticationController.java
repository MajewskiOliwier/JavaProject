package com.example.JavaProject.controller;

import com.example.JavaProject.dto.LoginDto;
import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.response.AuthenticationResponse;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto loginDto){
        return new ResponseEntity<>(authenticationService.login(loginDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        return new ResponseEntity<>(authenticationService.register(registerDto), HttpStatus.OK);
    }
}
