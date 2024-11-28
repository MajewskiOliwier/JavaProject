package com.example.JavaProject.controller;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.service.interfaces.ImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/recipes/import")  //TODO: rozwarz zmianę endpointów
@RequiredArgsConstructor
public class ImportController {

    private static final Logger logger = LoggerFactory.getLogger(ImportController.class);

    private final ImportService importService;

    @PostMapping("/json")
    public ResponseEntity<String> importRecipesFromJson(@RequestBody String json) {
        try {
            logger.info("Próba importu danych JSON: {}", json);
            List<RecipeDto> recipes = importService.importRecipesFromJson(json);

            // Jeśli nie ma żadnych przepisów po imporcie
            if (recipes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Brak danych do zaimportowania.");
            }

            // Zwracanie komunikatu o powodzeniu
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Przepisy zostały zaimportowane pomyślnie!");
        } catch (IOException e) {
            logger.error("Błąd przy imporcie JSON", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił błąd przy imporcie danych.");
        }
    }

    @PostMapping("/xml")
    public ResponseEntity<String> importRecipesFromXml(@RequestBody String xml) {
        try {
            logger.info("Próba importu danych XML: {}", xml);
            List<RecipeDto> recipes = importService.importRecipesFromXml(xml);

            // Jeśli nie ma żadnych przepisów po imporcie
            if (recipes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Brak danych do zaimportowania.");
            }

            // Zwracanie komunikatu o powodzeniu
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Przepisy zostały zaimportowane pomyślnie!");
        } catch (IOException e) {
            logger.error("Błąd przy imporcie XML", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił błąd przy imporcie danych.");
        }
    }
}