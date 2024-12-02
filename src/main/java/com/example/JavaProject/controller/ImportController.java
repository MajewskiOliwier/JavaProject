package com.example.JavaProject.controller;

import com.example.JavaProject.exception.ErrorInfo;
import com.example.JavaProject.service.interfaces.ImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Tag(name = "Import Recipes", description = "Endpoints for importing recipes from files")
public class ImportController {

    private final ImportService importService;

    @Operation(summary = "Import recipes from file", description = "Imports recipes from a JSON or XML file.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "File has been imported successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Unsupported file extension",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class)))
    })
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