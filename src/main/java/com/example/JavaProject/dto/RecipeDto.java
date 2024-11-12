package com.example.JavaProject.dto;

import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {
    private String recipeName;
    private int preparationTime;
    private int difficulty;
    private List<IngredientDto> ingredients;
    private String author;
    private int likes;
}
