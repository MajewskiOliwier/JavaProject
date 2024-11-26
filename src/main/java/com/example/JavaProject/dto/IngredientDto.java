package com.example.JavaProject.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
@JacksonXmlRootElement

public class IngredientDto {
    @NotNull
    @JacksonXmlProperty(localName = "ingredientName")
    private String ingredientName;

    @NotNull
    @Min(1)
    @JacksonXmlProperty(localName = "quantity")    private Float quantity;

    @NotNull
    private String measurement;
}
