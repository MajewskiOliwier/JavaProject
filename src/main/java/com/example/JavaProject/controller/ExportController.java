package com.example.JavaProject.controller;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.exception.ErrorInfo;
import com.example.JavaProject.service.interfaces.ExportService;
import com.example.JavaProject.service.interfaces.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/recipes/export")
@Tag(name = "Export Recipes", description = "Endpoints for exporting recipes in different formats")
public class ExportController {

    private final ExportService ExportService;
    private final RecipeService recipeService;

    @Autowired
    public ExportController(ExportService ExportService, RecipeService recipeService) {
        this.ExportService = ExportService;
        this.recipeService = recipeService;
    }

    @Operation(summary = "Export recipes", description = "Exports all recipes in the specified format (json or xml).")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Recipes exported successfully",
                    content = @Content(schema = @Schema(implementation = byte[].class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid file format",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class)))
    })
    @GetMapping
    public ResponseEntity<byte[]> exportRecipes(
                            @RequestParam(defaultValue = "json") final String format) throws IOException {

        List<RecipeDto> recipes = recipeService.getAllRecipeDtos();
        byte[] data;
        
        if (format.equalsIgnoreCase("xml")) {
            data = ExportService.exportRecipesToXml(recipes);
            HttpHeaders headers = getHeaders(MediaType.APPLICATION_XML, format);
            return new ResponseEntity<>(data, headers, HttpStatus.OK);

        } else if (format.equalsIgnoreCase("json")) {
            data = ExportService.exportRecipesToJson(recipes);
            HttpHeaders headers = getHeaders(MediaType.APPLICATION_JSON, format);
            return new ResponseEntity<>(data, headers, HttpStatus.OK);

        } else {
            throw new IllegalArgumentException("Invalid file format");
        }
    }

    private static HttpHeaders getHeaders(MediaType type, String format) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        headers.setContentDisposition(ContentDisposition
                                .attachment()
                                .filename("recipes." + format.toLowerCase())
                                .build());
        return headers;
    }
}