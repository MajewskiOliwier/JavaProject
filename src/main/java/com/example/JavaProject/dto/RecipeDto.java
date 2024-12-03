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

    @JsonProperty("recipeID")
    @JacksonXmlProperty(localName = "recipeID")
    private Long recipeID;

    @JsonProperty("recipeName")
    @JacksonXmlProperty(localName = "recipeName")
    @NotNull
    @Size(min=1, max = 50, message = "Recipe name length must be in range <1,50>")
    private String recipeName;

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

    @JsonProperty("ingredients")
    @JacksonXmlProperty(localName = "ingredients")
    private List<IngredientDto> ingredients;

    private String author;

    private int likes;

    @JsonProperty("recipeProcedures")
    @JacksonXmlProperty(localName = "recipeProcedures")
    @Size(min = 1, message = "At least one procedure step is required")
    private List<String> recipeProcedures;

    public RecipeDto(Recipe recipe) {
        this.recipeName = recipe.getRecipeName();
        this.preparationTime = recipe.getPreparationTime();
        this.difficulty = recipe.getDifficulty();
        this.recipeProcedures = recipe.getRecipeProcedures();
        this.ingredients = recipe.getRecipeIngredients().stream()
                .map(recipeIngredient -> new IngredientDto(
                        recipeIngredient.getIngredient().getIngredientName(),
                        recipeIngredient.getQuantity(),
                        recipeIngredient.getMeasurement()))
                .collect(Collectors.toList());
    }
}
