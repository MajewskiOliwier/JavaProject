package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RecipeDto;

import java.io.IOException;
import java.util.List;

public interface ImportExportService {
    byte[] exportRecipesToJson(List<RecipeDto> recipes) throws IOException;
    byte[] exportRecipesToXml(List<RecipeDto> recipes) throws IOException;
}