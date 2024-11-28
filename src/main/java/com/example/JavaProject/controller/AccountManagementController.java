package com.example.JavaProject.controller;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.service.interfaces.AccountManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountManagementController {

    private final AccountManagementService accountManagementService;

    @PutMapping
    public ResponseEntity<RegisterDto> updateAccount(@RequestBody @Valid RegisterDto registerDto){

        return new ResponseEntity<>(accountManagementService.updateAccount(registerDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAccount(){ //make fancy delete == hide

        return new ResponseEntity<>(accountManagementService.deleteAccount(), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<String> getAccountDetailsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(accountManagementService.getInfoByEmail(email));
    }

    //ADMIN ACTIONS: ✔️
    @PutMapping("/admin/hide/{id}")
    public ResponseEntity<String> hideAccount(@PathVariable long id,
                                              @RequestParam(defaultValue = "true") Boolean hidden){
        return new ResponseEntity<>(accountManagementService.hideAccount(id, hidden), HttpStatus.OK);
    }

    @PutMapping("/admin/promote/{id}")
    public ResponseEntity<String> promote(@PathVariable long id){
        return new ResponseEntity<>(accountManagementService.promote(id), HttpStatus.OK);
    }
}
