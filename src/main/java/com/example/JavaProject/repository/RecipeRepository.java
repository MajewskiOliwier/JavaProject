package com.example.JavaProject.repository;

import com.example.JavaProject.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findById(long id);

    @Query("SELECT COUNT(u) FROM Recipe r JOIN r.likedby u WHERE r.id = :recipeId")
    Long countLikesByRecipeId(Long recipeId);

    @Query("SELECT r FROM Recipe r JOIN r.recipeIngredients ri JOIN ri.ingredient i WHERE i.ingredientName LIKE %:ingredientName%")
    List<Recipe> findRecipesByIngredientName(@Param("ingredientName") String ingredientName);
}
