package com.example.JavaProject.controller;

import com.example.JavaProject.response.RecipeResponse;
import com.example.JavaProject.service.interfaces.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavouritesController {

    private final RecipeService recipeService;

    @PutMapping("/recipes/{id}/favourites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addRecipeToFavourite(@PathVariable long id) {
        String message = recipeService.addToFavourite(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/recipes/favourites")
    public ResponseEntity<List<RecipeResponse>> getFavouriteRecipes() {
        return new ResponseEntity<>(recipeService.getFavouriteRecipes(), HttpStatus.OK);
    }

    @DeleteMapping("/recipes/{id}/favourites")
    public ResponseEntity<String> deleteFavouriteRecipe(@PathVariable Long id) {
        String message = recipeService.deleteFavouriteRecipe(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
