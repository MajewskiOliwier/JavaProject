package com.example.JavaProject.controller;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.repository.IngredientRepository;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.response.LikesCountResponse;
import com.example.JavaProject.response.RecipeResponse;
import com.example.JavaProject.service.interfaces.AuthenticationService;
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
public class RecipeController {

        private final RecipeService recipeService;
        private final AuthenticationService authenticationService;

        @GetMapping("/recipes")
        public ResponseEntity<List<RecipeResponse>> getAllRecipe(){
            return new ResponseEntity<>(recipeService.getAllRecipes(), HttpStatus.OK);
        }

        @GetMapping("/recipes/{id}")
        public ResponseEntity<RecipeResponse> getAllRecipe(@PathVariable long id){
            return new ResponseEntity<>(recipeService.getRecipe(id), HttpStatus.OK);
        }

        @GetMapping("/recipes/{id}/likes")
        public ResponseEntity<LikesCountResponse> getRecipeLikeCount(@PathVariable long id){
            return new ResponseEntity<>(recipeService.getRecipeLikes(id), HttpStatus.OK);
        }

        @PostMapping("/recipes")
//        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<String> addRecipe(@RequestBody RecipeDto recipeDto){
            return new ResponseEntity<>(recipeService.addRecipe(recipeDto), HttpStatus.OK);
        }

        @PutMapping("/recipes/{id}")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<String> modifyRecipe(@PathVariable long id, @RequestBody RecipeDto recipeDto){
            return new ResponseEntity<>(recipeService.modifyRecipe(id, recipeDto), HttpStatus.OK);
        }

        @PutMapping("/recipes/{id}/likes")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<String> addRecipeLike(@PathVariable long id){
            return new ResponseEntity<>(recipeService.addLike(id), HttpStatus.OK);
        }

        @PutMapping("/recipes/{id}/favourite")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<String> addRecipeToFavourite(@PathVariable long id){
            return new ResponseEntity<>(recipeService.addToFavourite(id), HttpStatus.OK);
        }

        @GetMapping("/recipes/favourites")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<List<RecipeResponse>> getFavouriteRecipes(){
            return new ResponseEntity<>(recipeService.getFavouriteRecipes(), HttpStatus.OK);
        }

        @DeleteMapping("/recipes/{id}/favourite")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<String> deleteFavouriteRecipe(@PathVariable Long id){
            return new ResponseEntity<>(recipeService.deleteFavouriteRecipe(id), HttpStatus.OK);
        }
        @GetMapping("/recipes/search")
        public ResponseEntity<List<RecipeResponse>> findRecipesByIngredient(@RequestParam String ingredientName) {
        List<RecipeResponse> recipes = recipeService.findRecipesByIngredient(ingredientName);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }
}
