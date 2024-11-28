package com.example.JavaProject.mapper;

import com.example.JavaProject.dto.IngredientDto;
import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.RecipeIngredient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RecipeMapper {
    private final IngredientsMapper ingredientsMapper;
    public RecipeMapper(IngredientsMapper ingredientsMapper) {
        this.ingredientsMapper = ingredientsMapper;
    }

    public Recipe mapToEntity(RecipeDto recipeDto){
        Recipe newRecipe = new Recipe();
        newRecipe.setId(recipeDto.getId());
        newRecipe.setRecipeName(recipeDto.getRecipeName());
        newRecipe.setPreparationTime(recipeDto.getPreparationTime());
        newRecipe.setDifficulty(recipeDto.getDifficulty());
//        newRecipe.setIngredients(recipeDto.getIngredients());



        if (recipeDto.getIngredients() != null) {
            newRecipe.setRecipeIngredients(
                    recipeDto.getIngredients()
                            .stream()
                            .map(ingredientDto -> {
                                RecipeIngredient recipeIngredient = new RecipeIngredient();
                                recipeIngredient.setQuantity(ingredientDto.getQuantity());

                                Ingredient ingredient = new Ingredient();
                                ingredient.setIngredientName(ingredientDto.getIngredientName());
                                ingredient.setMeasurement(ingredientDto.getMeasurement());

                                recipeIngredient.setIngredient(ingredient);
                                recipeIngredient.setRecipe(newRecipe);

                                return recipeIngredient;
                            })
                            .collect(Collectors.toList())
            );
        }

        return newRecipe;
    }

//    public RecipeDto mapToDto(Recipe recipe){
//        RecipeDto newRecipeDto = new RecipeDto();
//        newRecipeDto.setRecipeName(recipe.getRecipeName());
//        newRecipeDto.setDifficulty(recipe.getDifficulty());
//        newRecipeDto.setPreparationTime(recipe.getPreparationTime());
//
//        if (recipe.getRecipeIngredients() != null) {
//            newRecipeDto.setIngredients(
//                    recipe.getRecipeIngredients()
//                            .stream()
//                            .map(recipeIngredient -> {
//                                IngredientDto ingredientDto = new IngredientDto();
//
//                                Ingredient ingredient = recipeIngredient.getIngredient();
//                                ingredientDto.setIngredientName(ingredient.getIngredientName());
//                                ingredientDto.setMesurement(ingredient.getMesurement());
//                                ingredientDto.setQuantity(recipeIngredient.getQuantity());
//
//                                return ingredientDto;
//                            })
//                            .collect(Collectors.toList())
//            );
//        }
//
//        return newRecipeDto;
//    }

    public RecipeDto mapToDto(Recipe recipe){
        RecipeDto newRecipeDto = new RecipeDto();
        newRecipeDto.setId(recipe.getId());
        newRecipeDto.setRecipeName(recipe.getRecipeName());
        newRecipeDto.setDifficulty(recipe.getDifficulty());
        newRecipeDto.setPreparationTime(recipe.getPreparationTime());
        newRecipeDto.setAuthor(recipe.getUser().getNormalUsername());
        newRecipeDto.setLikes((int)recipe.getLikedby().stream().count());

        if (recipe.getRecipeIngredients() != null && !recipe.getRecipeIngredients().isEmpty()) {
            newRecipeDto.setIngredients(
                    recipe.getRecipeIngredients()
                            .stream()
//                            .map(recipeIngredient -> {
//                                IngredientDto ingredientDto = new IngredientDto();
//
//                                Ingredient ingredient = recipeIngredient.getIngredient();
//                                ingredientDto.setIngredientName(ingredient.getIngredientName());
//                                ingredientDto.setMesurement(ingredient.getMesurement());
//                                ingredientDto.setQuantity(recipeIngredient.getQuantity());
//
//                                return ingredientDto;
//                            })
                            .map(ingredientsMapper::mapToDto)  // Use the new mapToDto method that includes quantity
                            .collect(Collectors.toList())
            );
        }

        return newRecipeDto;
    }

}
