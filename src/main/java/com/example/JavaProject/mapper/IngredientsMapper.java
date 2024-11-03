package com.example.JavaProject.mapper;

import com.example.JavaProject.dto.IngredientDto;
import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.RecipeIngredient;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class IngredientsMapper {
    public Ingredient mapToEntity(IngredientDto ingredientDto){
        Ingredient newIngredient = new Ingredient();
        newIngredient.setMeasurement(ingredientDto.getMeasurement());
        newIngredient.setIngredientName(ingredientDto.getIngredientName());

        return newIngredient;
    }

    public IngredientDto  mapToDto(RecipeIngredient recipeIngredient){
        IngredientDto ingredientDto = new IngredientDto();

        Ingredient ingredient = recipeIngredient.getIngredient();

        ingredientDto.setMeasurement(ingredient.getMeasurement());
        ingredientDto.setIngredientName(ingredient.getIngredientName());
        ingredientDto.setQuantity(recipeIngredient.getQuantity());

        return ingredientDto;
    }
}
