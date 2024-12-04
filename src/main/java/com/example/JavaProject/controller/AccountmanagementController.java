package com.example.JavaProject.controller;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.exception.ErrorInfo;
import com.example.JavaProject.service.interfaces.AccountManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "Endpoints for managing user accounts")
public class AccountmanagementController {

    private final AccountManagementService accountManagementService;

    @Operation(summary = "Update user account", description = "Updates the details of the current user account.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Account updated successfully",
                    content = @Content(schema = @Schema(implementation = RegisterDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with id: 1",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class)))
    })
    @PutMapping
    public ResponseEntity<RegisterDto> updateAccount(@RequestBody @Valid RegisterDto registerDto){
        return new ResponseEntity<>(accountManagementService.updateAccount(registerDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete user account", description = "Deletes (hides) the current user account.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User has been successfully deleted",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with email: email@example.com",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class)))
    })
    @DeleteMapping
    public ResponseEntity<String> deleteAccount(){ //make fancy delete == hide
        return new ResponseEntity<>(accountManagementService.deleteAccount(), HttpStatus.OK);
    }

    @Operation(summary = "Get account details by email", description = "Retrieves account details by email.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User is visible",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with id: 1",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class)))
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<String> getAccountDetailsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(accountManagementService.getInfoByEmail(email));
    }

    //ADMIN ACTIONS: ✔️
    @Operation(summary = "Admin - hide or unhide user account", description = "Admni can hides or unhides a user account by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User account has been successfully hidden/unhidden.",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with id: 1",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class)))
    })
    @PutMapping("/admin/hide/{id}")
    public ResponseEntity<String> hideAccount(@PathVariable long id){
        return new ResponseEntity<>(accountManagementService.hideAccount(id), HttpStatus.OK);
    }

    @Operation(summary = "Admin -promote user to admin", description = "Admin can promotes a user to admin by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User promoted successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "User cannot promote oneself or user is already an admin",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with id: 1",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class)))
    })
    @PutMapping("/admin/promote/{id}")
    public ResponseEntity<String> promote(@PathVariable long id){
        return new ResponseEntity<>(accountManagementService.promote(id), HttpStatus.OK);
    }
}
