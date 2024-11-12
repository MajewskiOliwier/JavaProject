package com.example.JavaProject.controller;

import com.example.JavaProject.dto.LoginDto;
import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.response.AuthenticationResponse;
import com.example.JavaProject.response.LikesCountResponse;
import com.example.JavaProject.service.interfaces.AccountManagementService;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AuthenticationService authenticationService;
    private final AccountManagementService accountManagementService;

    @PutMapping("/admin/promote/{id}")
    public ResponseEntity<String> promote(@PathVariable long id){
        return new ResponseEntity<>(accountManagementService.promote(id), HttpStatus.OK);
    }

//    @PutMapping("/")
//    public ResponseEntity<LikesCountResponse> getRecipeLikeCount(@RequestBody RegisterDto registerDto){
//        return new ResponseEntity<>(recipeService.getRecipeLikes(id), HttpStatus.OK);
//    }


}
