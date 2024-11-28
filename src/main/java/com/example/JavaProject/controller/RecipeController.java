package com.example.JavaProject.controller;

import com.example.JavaProject.dto.RecipeDto;

import com.example.JavaProject.response.RecipeResponse;
import com.example.JavaProject.service.interfaces.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAllRecipe() {
        return new ResponseEntity<>(recipeService.getAllRecipes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getAllRecipe(@PathVariable long id) {
        return new ResponseEntity<>(recipeService.getRecipe(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addRecipe(@RequestBody RecipeDto recipeDto) {
        return new ResponseEntity<>(recipeService.addRecipe(recipeDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifyRecipe(@PathVariable long id, @RequestBody RecipeDto recipeDto) {
        return new ResponseEntity<>(recipeService.modifyRecipe(id, recipeDto), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeResponse>> findRecipesByIngredient(@RequestParam String ingredientName) {
        List<RecipeResponse> recipes = recipeService.findRecipesByIngredient(ingredientName);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }
}
