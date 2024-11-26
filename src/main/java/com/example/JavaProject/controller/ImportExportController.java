package com.example.JavaProject.controller;

import com.example.JavaProject.service.interfaces.ImportExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/import-export")
@RequiredArgsConstructor
public class ImportExportController {

    private final ImportExportService importExportService;

    @GetMapping("/recipes/export/json")
    public ResponseEntity<byte[]> exportRecipesToJson() throws IOException {
        byte[] data = importExportService.exportRecipesToJson();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDisposition(ContentDisposition.attachment().filename("recipes.json").build());
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @GetMapping("/recipes/export/xml")
    public ResponseEntity<byte[]> exportRecipesToXml() throws IOException {
        byte[] data = importExportService.exportRecipesToXml();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDisposition(ContentDisposition.attachment().filename("recipes.xml").build());
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @PostMapping("/recipes/import/json")
    public ResponseEntity<String> importRecipesFromJson(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Plik jest pusty", HttpStatus.BAD_REQUEST);
        }
        String message = importExportService.importRecipesFromJson(file.getBytes());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/recipes/import/xml")
    public ResponseEntity<String> importRecipesFromXml(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Plik jest pusty", HttpStatus.BAD_REQUEST);
        }
        String message = importExportService.importRecipesFromXml(file.getBytes());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
