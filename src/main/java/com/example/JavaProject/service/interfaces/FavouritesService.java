package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.response.RecipeResponse;

import java.util.List;

public interface FavouritesService {

    String addToFavourite(long id);

    List<RecipeResponse> getFavouriteRecipes();

    String deleteFavouriteRecipe(Long id);
}
