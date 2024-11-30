package com.example.JavaProject.mapper;

import com.example.JavaProject.dto.IngredientDto;
import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.entity.RecipeIngredient;
import com.example.JavaProject.response.IngredientResponse;
import org.springframework.stereotype.Component;

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

    public IngredientResponse  mapToResponse(Ingredient ingredient){
        IngredientResponse ingredientResponse = new IngredientResponse();

        ingredientResponse.setMeasurement(ingredient.getMeasurement());
        ingredientResponse.setIngredientName(ingredient.getIngredientName());

        return ingredientResponse;
    }
}
