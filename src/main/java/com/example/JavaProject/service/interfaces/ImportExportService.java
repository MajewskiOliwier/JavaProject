package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RecipeDto;
import java.io.IOException;
import java.util.List;

public interface ImportExportService {
    byte[] exportRecipesToJson() throws IOException;
    byte[] exportRecipesToXml() throws IOException;
    String importRecipesFromJson(byte[] data) throws IOException;
    String importRecipesFromXml(byte[] data) throws IOException;
}