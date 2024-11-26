package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.service.interfaces.ImportExportService;
import com.example.JavaProject.service.interfaces.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api")
@Service
@RequiredArgsConstructor
public class ImportExportServiceImpl implements ImportExportService {

    private final RecipeService recipeService;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;

    @Override
    public byte[] exportRecipesToJson() throws IOException {
        List<RecipeDto> recipes = recipeService.getAllRecipeDtos();
        return objectMapper.writeValueAsBytes(recipes);
    }

    @Override
    public byte[] exportRecipesToXml() throws IOException {
        List<RecipeDto> recipes = recipeService.getAllRecipeDtos();
        return xmlMapper.writeValueAsBytes(recipes);
    }

    @Override
    public String importRecipesFromJson(byte[] data) throws IOException {
        // Logowanie rozmiaru otrzymanego pliku
        System.out.println("Received JSON file with size: " + data.length);

        List<RecipeDto> recipes = objectMapper.readValue(data, new TypeReference<List<RecipeDto>>() {});
        recipeService.saveAll(recipes);

        return "Import zakończony sukcesem";
    }

    @Override
    public String importRecipesFromXml(byte[] data) throws IOException {
        System.out.println("Received XML file with size: " + data.length);

        List<RecipeDto> recipes = xmlMapper.readValue(data, new TypeReference<List<RecipeDto>>() {});
        recipeService.saveAll(recipes);

        return "Import zakończony sukcesem";
    }
}
