package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.response.RecipeResponse;

import java.util.List;

public interface RecipeService {

    public List<RecipeResponse> getAllRecipes();

    RecipeResponse getRecipe(long id);

    String addRecipe(RecipeDto recipeDto);

    String deleteRecipe(long id);

    String modifyRecipe(long id, RecipeDto recipeDto);

    List<RecipeResponse> findRecipesByIngredient(String ingredientName);

    List<RecipeDto> getAllRecipeDtos();


}
