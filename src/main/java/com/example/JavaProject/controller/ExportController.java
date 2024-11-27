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

    @GetMapping("/json")
    public ResponseEntity<byte[]> exportRecipesToJson() throws IOException {
        // Pobierz przepisy za pomocą RecipeService
        List<RecipeDto> recipes = recipeService.getAllRecipeDtos();  // To pobiera przepisy jako RecipeDto
        byte[] data = ExportService.exportRecipesToJson(recipes);  // Używamy metody serwisu do eksportu do JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDisposition(ContentDisposition.attachment().filename("recipes.json").build());
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @GetMapping("/xml")
    public ResponseEntity<byte[]> exportRecipesToXml() throws IOException {
        // Pobierz przepisy za pomocą RecipeService
        List<RecipeDto> recipes = recipeService.getAllRecipeDtos();  // To pobiera przepisy jako RecipeDto
        byte[] data = ExportService.exportRecipesToXml(recipes);  // Używamy metody serwisu do eksportu do XML
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDisposition(ContentDisposition.attachment().filename("recipes.xml").build());
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
