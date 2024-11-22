package com.example.JavaProject.controller;

import com.example.JavaProject.dto.LoginDto;
import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/test-data.sql")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenValidInput_thenCreateUser() throws Exception {
        RegisterDto register = new RegisterDto("newUser", 20, true, "email123@email", "password");

         var result = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

         result
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8"))
                 .andExpect(content().string("USER REGISTRATION SUCCESSFUL"));

        List<User> all = userRepository.findAll();
        assertThat(all).extracting(User::getNormalUsername).contains("newUser");
    }

    @Test
    public void whenInvalidInput_thenErrorRegisterMessageAppears() throws Exception {
        RegisterDto register = new RegisterDto("username", 20, true, "invalidEmail", "password");

        var result = mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

        result
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors.email", is("Invalid email format")));

        List<User> all = userRepository.findAll();
        assertThat(all).extracting(User::getEmail).doesNotContain("invalidEmail");
    }

    @Test
    public void whenValidInput_thenLogin() throws Exception {

        var login = new LoginDto("email@email.com", 30, "password");

        var result = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.tokenType", is("ROLE_USER")));
    }

    @Test
    public void whenInvalidEmail_thenUserNotFound() throws Exception {
        var login = new LoginDto("email", 20, "password");

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
        var login = new LoginDto("email@email.com", 30, "badPassword");

        var result = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("Invalid password")));
    }
}