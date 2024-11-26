package com.example.JavaProject.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
@JacksonXmlRootElement
public class RecipeDto {
    @JacksonXmlProperty
    @NotNull
    @Size(min=1, max = 50, message = "Recipe name length must be in range <1,50>")
    private String recipeName;

    @Min(1)
    @NotNull
    @JacksonXmlProperty
    private int preparationTime;

    @Min(1)
    @Max(5)
    @JacksonXmlProperty
    private int difficulty;

    @JacksonXmlProperty
    private List<IngredientDto> ingredients;

    @JacksonXmlProperty
    private String author;

    @JacksonXmlProperty
    private int likes;
}
