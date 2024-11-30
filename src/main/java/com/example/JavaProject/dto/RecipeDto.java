package com.example.JavaProject.dto;

import com.example.JavaProject.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {


    @JsonProperty("id")
    @JacksonXmlProperty(localName = "id")
    @NotNull
    private Long id;

    @JsonProperty("recipeName")
    @JacksonXmlProperty(localName = "recipeName")
    @NotNull
    @Size(min=1, max = 50, message = "Recipe name length must be in range <1,50>")
    private String recipeName;

    private String author;

    @JsonProperty("preparationTime")
    @JacksonXmlProperty(localName = "preparationTime")
    @Min(1)
    @NotNull
    private int preparationTime;

    @JsonProperty("difficulty")
    @JacksonXmlProperty(localName = "difficulty")
    @Min(1)
    @Max(5)
    private int difficulty;

    private int likes;

    @JsonProperty("ingredients")
    @JacksonXmlProperty(localName = "ingredients")
    private List<IngredientDto> ingredients;

    public RecipeDto(Recipe recipe) {
        this.id = recipe.getId();
        this.recipeName = recipe.getRecipeName();
        this.preparationTime = recipe.getPreparationTime();
        this.difficulty = recipe.getDifficulty();
        this.ingredients = recipe.getRecipeIngredients().stream()
                .map(recipeIngredient -> new IngredientDto(
                        recipeIngredient.getIngredient().getIngredientName(),
                        recipeIngredient.getQuantity(),
                        recipeIngredient.getMeasurement()))
                .collect(Collectors.toList());
    }
}
