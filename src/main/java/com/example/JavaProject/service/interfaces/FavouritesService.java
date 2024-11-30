package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.dto.RecipeDto;

import java.util.List;

public interface FavouritesService {

    String addToFavourite(long id);

    List<RecipeDto> getFavouriteRecipes();

    String deleteFavouriteRecipe(Long id);
}
