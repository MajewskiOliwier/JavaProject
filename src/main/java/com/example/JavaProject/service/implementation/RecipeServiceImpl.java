package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.IngredientDto;
import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.RecipeIngredient;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.exception.ProfileHiddenException;
import com.example.JavaProject.exception.RecipeNotFoundException;
import com.example.JavaProject.mapper.IngredientsMapper;
import com.example.JavaProject.mapper.RecipeMapper;
import com.example.JavaProject.repository.IngredientRepository;
import com.example.JavaProject.repository.RecipeIngredientRepository;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import com.example.JavaProject.service.interfaces.RecipeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private RecipeIngredientRepository recipeIngredientRepository;
    private UserRepository userRepository;
    private RecipeMapper recipeMapper;
    private IngredientsMapper ingredientsMapper;
    private AuthenticationService authenticationService;

    @Override
    public List<RecipeDto> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return getMappedRecipeDtos(recipes);
    }


    @Override
    public RecipeDto getRecipe(long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                                () -> new RecipeNotFoundException(id));

        isHidden(recipe);

        return recipeMapper.mapToDto(recipe);
    }

    @Override
    public String addRecipe(RecipeDto recipeDto) {
        Long userId = authenticationService.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow(
                        () -> new RuntimeException("User not found"));

        if (user.isHidden()) throw new ProfileHiddenException();

        Recipe recipe = recipeMapper.mapToEntity(recipeDto);

        AddNewIngredients(recipeDto, recipe);
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

        recipe.setUser(user);
        for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
            Ingredient existingIngredient = ingredientRepository
                    .findByIngredientNameAndMeasurement(ingredientDto.getIngredientName(), ingredientDto.getMeasurement());

            if (existingIngredient == null) {
                existingIngredient = ingredientRepository.save(ingredientsMapper.mapToEntity(ingredientDto));
            }

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setQuantity(ingredientDto.getQuantity());
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(existingIngredient);

            recipeIngredients.add(recipeIngredient);
        }

        recipe.setRecipeIngredients(recipeIngredients);

        recipeRepository.save(recipe);
        recipeIngredientRepository.saveAll(recipeIngredients);

        return "Added new recipe";
    }

    @Transactional
    @Override
    public String modifyRecipe(long id, RecipeDto recipeDto) {
        Recipe updatedRecipe = recipeRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("ID not found"));

        if (!isOwner(updatedRecipe))
                throw new AccessDeniedException("User can only modify their recipe");

        isHidden(updatedRecipe);


        setRecipeFields(recipeDto, updatedRecipe);

        List<RecipeIngredient> existingIngredients = new ArrayList<>(updatedRecipe.getRecipeIngredients());

        RemoveOldIngredients(recipeDto, existingIngredients);
        AddNewIngredients(recipeDto, updatedRecipe);

        return "Recipe is modified";
    }

    private static void setRecipeFields(RecipeDto recipeDto, Recipe updatedRecipe) {
        updatedRecipe.setRecipeName(recipeDto.getRecipeName());
        updatedRecipe.setDifficulty(recipeDto.getDifficulty());
        updatedRecipe.setPreparationTime(recipeDto.getPreparationTime());
    }

    @Override
    public List<RecipeDto> findRecipesByIngredient(String ingredientName) {
        List<Recipe> recipes = recipeRepository.findRecipesByIngredientName(ingredientName);

        return getMappedRecipeDtos(recipes);
    }

    private boolean isOwner(Recipe updatedRecipe) {
        return Objects.equals(updatedRecipe.getUser().getId(), authenticationService.getCurrentUserId());
    }

    public static boolean checkIfHidden(Recipe updatedRecipe) {
        return updatedRecipe.getUser().isHidden();
    }

    public static void isHidden(Recipe recipe) {
        if(recipe.getUser().isHidden() == true){
            throw new ProfileHiddenException();
        }
    }

    private void AddNewIngredients(RecipeDto recipeDto, Recipe updatedRecipe) {
        for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientName(ingredientDto.getIngredientName());
            ingredient.setMeasurement(ingredientDto.getMeasurement());

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setQuantity(ingredientDto.getQuantity());
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setRecipe(updatedRecipe);

            ingredient.getIngredientsRecipe().add(recipeIngredient);

            ingredientRepository.save(ingredient);
            recipeIngredientRepository.save(recipeIngredient);
        }
    }

    private void RemoveOldIngredients(RecipeDto recipeDto, List<RecipeIngredient> existingIngredients) {

        for (RecipeIngredient recipeIngredient : existingIngredients) {
            boolean unchangedIngredient = recipeDto.getIngredients()
                    .stream()
                    .anyMatch(ingredientDto -> isSameIngredient(recipeIngredient, ingredientDto));

            if (unchangedIngredient) continue;

            recipeIngredientRepository.delete(recipeIngredient);
        }
    }

    private static boolean isSameIngredient(RecipeIngredient recipeIngredient, IngredientDto ingredientDto) {
        return ingredientDto.getIngredientName().equalsIgnoreCase(recipeIngredient.getIngredient().getIngredientName()) &&
                ingredientDto.getMeasurement().equalsIgnoreCase(recipeIngredient.getIngredient().getMeasurement());
    }

    private List<RecipeDto> getMappedRecipeDtos(List<Recipe> recipes) {

        return recipes.stream()
                .filter(recipe -> !checkIfHidden(recipe))
                .map(recipeMapper::mapToDto)
                .toList();
    }
}