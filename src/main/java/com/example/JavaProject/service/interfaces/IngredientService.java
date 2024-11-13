package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.response.IngredientResponse;

import java.util.List;

public interface IngredientService {
    List<IngredientResponse> getIngredients(String phrase);
}
