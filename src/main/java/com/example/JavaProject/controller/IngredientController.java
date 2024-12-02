package com.example.JavaProject.controller;

import com.example.JavaProject.response.IngredientResponse;
import com.example.JavaProject.exception.ErrorInfo;
import com.example.JavaProject.service.interfaces.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@Tag(name = "Ingredients", description = "Endpoints for managing ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Operation(summary = "Get all ingredients", description = "Retrieves a list of all ingredients, optionally filtered by a search phrase.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ingredients retrieved successfully",
                    content = @Content(schema = @Schema(implementation = IngredientResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class)))
    })
    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getAllIngredients(
                                    @RequestParam(defaultValue = "") String phrase) {
        return new ResponseEntity<>(ingredientService.getIngredients(phrase), HttpStatus.OK);
    }
}