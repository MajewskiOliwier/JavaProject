package com.example.JavaProject.controller;

import com.example.JavaProject.response.RecipeResponse;
import com.example.JavaProject.service.interfaces.FavouritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class FavouritesController {

    private final FavouritesService favouritesService;

    @PutMapping("/{id}/favourites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addRecipeToFavourite(@PathVariable long id) {
        String message = favouritesService.addToFavourite(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/favourites")
    public ResponseEntity<List<RecipeResponse>> getFavouriteRecipes() {
        return new ResponseEntity<>(favouritesService.getFavouriteRecipes(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/favourites")
    public ResponseEntity<String> deleteFavouriteRecipe(@PathVariable Long id) {
        String message = favouritesService.deleteFavouriteRecipe(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
