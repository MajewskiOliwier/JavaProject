package com.example.JavaProject.controller;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.repository.IngredientRepository;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.response.RecipeResponse;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import com.example.JavaProject.service.interfaces.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecipeController {

        private final RecipeService recipeService;

        @GetMapping("/recipes")
        public ResponseEntity<List<RecipeResponse>> getAllRecipe(){
            return new ResponseEntity<>(recipeService.getAllRecipes(), HttpStatus.OK);
        }

        @GetMapping("/recipes/{id}")
        public ResponseEntity<RecipeResponse> getAllRecipe(@PathVariable long id){
            return new ResponseEntity<>(recipeService.getRecipe(id), HttpStatus.OK);
        }

        @PostMapping("/recipes")
        public ResponseEntity<String> addRecipe(@RequestBody RecipeDto recipeDto){
            return new ResponseEntity<>(recipeService.addRecipe(recipeDto), HttpStatus.OK);
        }

        @PutMapping("/recipes/{id}")
        public ResponseEntity<String> addRecipe(@PathVariable long id, @RequestBody RecipeDto recipeDto){
            return new ResponseEntity<>(recipeService.modifyRecipe(id, recipeDto), HttpStatus.OK);
        }

}
