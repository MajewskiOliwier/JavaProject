package com.example.JavaProject.response;

import com.example.JavaProject.dto.IngredientDto;
import com.example.JavaProject.entity.RecipeIngredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {
    private String recipeName;
    private int preparationTime;
    private int difficulty;
    private List<IngredientDto> ingredients;
}
