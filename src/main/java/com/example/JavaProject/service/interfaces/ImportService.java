package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RecipeDto;

import java.io.IOException;
import java.util.List;

public interface ImportService {

    List<RecipeDto> importRecipesFromJson(String json) throws IOException;

    List<RecipeDto> importRecipesFromXml(String xml) throws IOException;
}