package com.example.JavaProject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {

    @JsonProperty("ingredientName")
    @JacksonXmlProperty(localName = "ingredientName")
    private String ingredientName;

    @JsonProperty("quantity")
    @JacksonXmlProperty(localName = "quantity")
    private Double quantity;

    @JsonProperty("measurement")
    @JacksonXmlProperty(localName = "measurement")
    private String measurement;  // Dodane pole measurement
}
