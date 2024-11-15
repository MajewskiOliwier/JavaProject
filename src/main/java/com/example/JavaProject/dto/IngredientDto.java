package com.example.JavaProject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {
    @NotNull
    private String ingredientName;

    @NotNull
    @Min(1)
    private Float quantity;

    @NotNull
    private String measurement;
}
