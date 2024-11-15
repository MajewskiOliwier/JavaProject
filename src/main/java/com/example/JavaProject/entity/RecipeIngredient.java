package com.example.JavaProject.entity;
import jakarta.persistence.*;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipe_ingredient")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
}