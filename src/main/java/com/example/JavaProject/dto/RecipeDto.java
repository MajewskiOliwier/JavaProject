package com.example.JavaProject.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {
    @NotNull
    @Size(min=1, max = 50, message = "Recipe name length must be in range <1,50>")
    private String recipeName;

    @Min(1)
    @NotNull
    private int preparationTime;

    @Min(1)
    @Max(5)
    private int difficulty;

    private List<IngredientDto> ingredients;

    private String author;

    private int likes;
}
