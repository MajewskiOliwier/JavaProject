package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.service.interfaces.ImportService;
import com.example.JavaProject.service.interfaces.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
public class ImportServiceImpl implements ImportService {

    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;
    private final RecipeService recipeService;

    public ImportServiceImpl(ObjectMapper objectMapper, XmlMapper xmlMapper, RecipeService recipeService) {
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
        this.recipeService = recipeService;
    }


    @Override
    public void importRecipesFromJson(MultipartFile file) throws IOException {
            List<RecipeDto> recipes =  objectMapper.readValue(
                    file.getBytes(),
                    objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, RecipeDto.class));

            recipes.forEach(recipeService::addRecipe);
    }

    @Override
    public void importRecipesFromXml(MultipartFile file) throws IOException{
            List<RecipeDto> recipes = xmlMapper.readValue(
                    file.getBytes(),
                    xmlMapper.getTypeFactory()
                            .constructCollectionType(List.class, RecipeDto.class));

            recipes.forEach(recipeService::addRecipe);
    }
}