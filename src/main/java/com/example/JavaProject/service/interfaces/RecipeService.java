package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RecipeDto;

import java.util.List;

public interface RecipeService {

    List<RecipeDto> getAllRecipes();

    RecipeDto getRecipe(long id);

    String addRecipe(RecipeDto recipeDto);

    String modifyRecipe(long id, RecipeDto recipeDto);

    List<RecipeDto> findRecipesByIngredient(String ingredientName);

    List<RecipeDto> getAllRecipeDtos();


}
