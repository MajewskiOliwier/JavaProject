package com.example.JavaProject.mapper;

import com.example.JavaProject.dto.IngredientDto;
import com.example.JavaProject.entity.RecipeIngredient;
import org.springframework.stereotype.Component;

@Component
public class RecipeIngredientMapper {
    public IngredientDto mapToDto(RecipeIngredient recipeIngredient) {
        IngredientDto ingredientDto = new IngredientDto();

        ingredientDto.setIngredientName(recipeIngredient.getIngredient().getIngredientName());
        ingredientDto.setMeasurement(recipeIngredient.getIngredient().getMeasurement());
        ingredientDto.setQuantity(recipeIngredient.getQuantity());

        return ingredientDto;
    }

}
