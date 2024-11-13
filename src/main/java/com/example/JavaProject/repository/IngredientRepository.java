package com.example.JavaProject.repository;

import com.example.JavaProject.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Ingredient findByIngredientNameAndMeasurement(String ingredient, String measurement);

    List<Ingredient> findAllByIngredientNameContainingIgnoreCase(String ingredientName);

}
