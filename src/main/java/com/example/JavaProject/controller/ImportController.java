package com.example.JavaProject.controller;

import com.example.JavaProject.service.interfaces.ImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;

    @PostMapping("/file")
        public ResponseEntity<String> importRecipesFromJson(@RequestPart MultipartFile file) throws IOException {

        String fileExtension = file.getOriginalFilename()
                                    .substring(file.getOriginalFilename().indexOf(".") + 1)
                                    .toLowerCase();

        if(fileExtension.equalsIgnoreCase("JSON")){
            importService.importRecipesFromJson(file);

        } else if (fileExtension.equalsIgnoreCase("XML")){
            importService.importRecipesFromXml(file);

        } else {
            throw new RuntimeException("Unsupported file extension");
        }

        return ResponseEntity.ok("File has been imported successfully");
    }
}