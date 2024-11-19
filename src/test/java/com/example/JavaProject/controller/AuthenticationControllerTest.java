package com.example.JavaProject.controller;

import com.example.JavaProject.dto.LoginDto;
import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.Role;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.service.implementation.JwtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtServiceImpl jwtServiceImpl;

    @Test
    public void whenValidInput_thenCreateUser() throws Exception {
        RegisterDto register = new RegisterDto("username", 20, true, "email", "password");

         var result = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

         result
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8"))
                 .andExpect(content().string("USER REGISTRATION SUCCESSFUL"));

        verify(userRepository, times(1)).save(any());
    }

    @Test
    @Disabled
    public void whenInvalidInput_thenErrorRegisterMessageAppears() throws Exception {
        RegisterDto register = new RegisterDto("username", 20, true, "email", "password");

        var result = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

        result
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").isString());
    }

    @Test
    public void whenValidInput_thenLogin() throws Exception {

        var login = new LoginDto("email", 20, "password");
        var user = User.builder()
                        .email(login.getEmail())
                        .age(login.getAge())
                        .password(login.getPassword())
                        .role(new Role(0L, "USER", null))
                        .build();

        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtServiceImpl.generateToken(any())).thenReturn("token");

        var result = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.accessToken", is("token")))
                .andExpect(jsonPath("$.tokenType", is("USER")));
    }

    @Test
    public void whenInvalidEmail_thenUserNotFound() throws Exception {
        var login = new LoginDto("email", 20, "password");

        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.empty());

        var result = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));

        result
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("User not found")));
    }

    @Test
    public void whenInvalidPassword_thenInvalidPasswordMessage() throws Exception {
        var login = new LoginDto("email", 20, "password");
        var user = User.builder()
                .email(login.getEmail())
                .age(login.getAge())
                .password(login.getPassword())
                .role(new Role(0L, "USER", null))
                .build();

        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        var result = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("Invalid password")));
    }
}