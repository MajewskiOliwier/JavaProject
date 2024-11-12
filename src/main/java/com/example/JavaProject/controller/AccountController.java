package com.example.JavaProject.controller;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.service.interfaces.AccountManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountManagementService accountManagementService;

    @PutMapping("/admin/promote/{id}")
    public ResponseEntity<String> promote(@PathVariable long id){
        return new ResponseEntity<>(accountManagementService.promote(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping
    public ResponseEntity<RegisterDto> updateAccount(@RequestBody RegisterDto registerDto){

        return new ResponseEntity<>(accountManagementService.updateAccount(registerDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping
    public ResponseEntity<String> deleteAccount(){ //make fancy delete == hide

        return new ResponseEntity<>(accountManagementService.deleteAccount(), HttpStatus.OK);
    }
}
