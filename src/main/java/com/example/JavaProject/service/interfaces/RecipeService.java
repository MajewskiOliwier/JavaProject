package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.response.LikesCountResponse;
import com.example.JavaProject.response.RecipeResponse;

import java.util.List;

public interface RecipeService {

    public List<RecipeResponse> getAllRecipes();

    RecipeResponse getRecipe(long id);

    LikesCountResponse getRecipeLikes(long id);

    String addRecipe(RecipeDto recipeDto);

    String modifyRecipe(long id, RecipeDto recipeDto);

    String addLike(long id);

    String removeLike(long id);

    String addToFavourite(long id);

    List<RecipeResponse> getFavouriteRecipes();

    String deleteFavouriteRecipe(Long id);

    List<RecipeResponse> findRecipesByIngredient(String ingredientName);

//    void saveAll(List<RecipeDto> recipes);

    List<RecipeDto> getAllRecipeDtos();


}
