package com.example.JavaProject.controller;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.service.interfaces.ExportService;
import com.example.JavaProject.service.interfaces.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/recipes/export")
public class ExportController {

    private final ExportService ExportService;
    private final RecipeService recipeService;

    @Autowired
    public ExportController(ExportService ExportService, RecipeService recipeService) {
        this.ExportService = ExportService;
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<byte[]> exportRecipes(
                            @RequestParam(defaultValue = "json") final String format) throws IOException {
        
        List<RecipeDto> recipes = recipeService.getAllRecipes();
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
