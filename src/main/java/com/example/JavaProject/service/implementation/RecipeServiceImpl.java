package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.IngredientDto;
import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.RecipeIngredient;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.exception.ProfileHiddenException;
import com.example.JavaProject.mapper.IngredientsMapper;
import com.example.JavaProject.mapper.RecipeMapper;
import com.example.JavaProject.repository.IngredientRepository;
import com.example.JavaProject.repository.RecipeIngredientRepository;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.response.RecipeResponse;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import com.example.JavaProject.service.interfaces.RecipeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<RecipeResponse> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        List<RecipeResponse> recipeResponses = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (isHidden(recipe)) {
                continue;
            }

            RecipeDto recipeDto = recipeMapper.mapToDto(recipe);
            List<IngredientDto> ingredientDtos = new ArrayList<>();
            for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
                ingredientDtos.add(ingredientsMapper.mapToDto(recipeIngredient));
            }

            RecipeResponse recipeResponse = getRecipeResponse(recipeDto, ingredientDtos);
            recipeResponses.add(recipeResponse);
        }
        return recipeResponses;
    }


    @Override
    public RecipeResponse getRecipe(long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if (recipe.isEmpty()) {
//           throw Exception("custome Exception");
        }

        Recipe foundRecipe = recipe.get();
        if (isHidden(foundRecipe)) {
            throw new ProfileHiddenException("Profile has been deleted");
        }

        RecipeDto recipeDto = recipeMapper.mapToDto(foundRecipe);
        List<IngredientDto> ingredientDtos = recipeDto.getIngredients();

        return getRecipeResponse(recipeDto, ingredientDtos);
    }

    private static RecipeResponse getRecipeResponse(RecipeDto recipeDto, List<IngredientDto> ingredientDtos) {
        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setRecipeID(recipeDto.getRecipeID() != null ? recipeDto.getRecipeID() : 0L);
        recipeResponse.setRecipeName(recipeDto.getRecipeName());
        recipeResponse.setDifficulty(recipeDto.getDifficulty());
        recipeResponse.setPreparationTime(recipeDto.getPreparationTime());
        recipeResponse.setAuthor(recipeDto.getAuthor());
        recipeResponse.setLikes(recipeDto.getLikes());
        recipeResponse.setIngredients(ingredientDtos);
        recipeResponse.setProcedures(recipeDto.getRecipeProcedures());
        return recipeResponse;
    }
    @Override
    public String addRecipe(RecipeDto recipeDto) {
        Long userId = authenticationService.getCurrentUserId();

        if (userId == null) {
            return "User not logged in";
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isHidden()) {
            return "User is banned";
        }

        Recipe recipe = recipeMapper.mapToEntity(recipeDto);
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
        recipe.setRecipeProcedures(recipeDto.getRecipeProcedures());

        recipeRepository.save(recipe);
        recipeIngredientRepository.saveAll(recipeIngredients);

        return "Added new recipe";
    }

    @Transactional
    @Override
    public String modifyRecipe(long id, RecipeDto recipeDto) {
        Recipe updatedRecipe = recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("ID not found"));

        if (!isOwner(updatedRecipe)) {
            return "User can only modify their recipe";
        }

        if (isHidden(updatedRecipe)) {
            return "User is banned";
        }


        UpdateRecipeData(recipeDto, updatedRecipe);

        List<RecipeIngredient> existingIngredients = new ArrayList<>(updatedRecipe.getRecipeIngredients());

        RemoveOldIngredients(recipeDto, existingIngredients);
        AddNewIngredients(recipeDto, updatedRecipe);

        return "Recipe is modified";
    }

    private boolean isOwner(Recipe updatedRecipe) {
        return Objects.equals(updatedRecipe.getUser().getId(), authenticationService.getCurrentUserId());
    }

    public static boolean isHidden(Recipe updatedRecipe) {
        return updatedRecipe.getUser().isHidden();
    }

    private static void UpdateRecipeData(RecipeDto recipeDto, Recipe updatedRecipe) {
        updatedRecipe.setRecipeName(recipeDto.getRecipeName());
        updatedRecipe.setDifficulty(recipeDto.getDifficulty());
        updatedRecipe.setPreparationTime(recipeDto.getPreparationTime());
        updatedRecipe.setRecipeProcedures(recipeDto.getRecipeProcedures());
    }

    private void AddNewIngredients(RecipeDto recipeDto, Recipe updatedRecipe) {
        for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientName(ingredientDto.getIngredientName());
            ingredient.setMeasurement(ingredientDto.getMeasurement());
            ingredient.setIngredientsRecipe(new ArrayList<>());

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
                    .anyMatch(ingredientDto -> (ingredientDto.getIngredientName().equalsIgnoreCase(recipeIngredient.getIngredient().getIngredientName())
                            && ingredientDto.getMeasurement().equalsIgnoreCase(recipeIngredient.getIngredient().getMeasurement())));

            if (!unchangedIngredient) {
                recipeIngredient.getRecipe().getRecipeIngredients().remove(recipeIngredient);
                recipeIngredient.getIngredient().getIngredientsRecipe().remove(recipeIngredient);
                recipeIngredient.setRecipe(null);
                recipeIngredient.setIngredient(null);

                recipeIngredientRepository.delete(recipeIngredient);
            }
        }
    }

    @Override
    public List<RecipeResponse> findRecipesByIngredient(String ingredientName) {
        List<Recipe> recipes = recipeRepository.findRecipesByIngredientName(ingredientName);

        List<RecipeResponse> recipeResponses = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (isHidden(recipe)) {
                continue;
            }

            RecipeDto recipeDto = recipeMapper.mapToDto(recipe);

            List<IngredientDto> ingredientDtos = new ArrayList<>();
            for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
                ingredientDtos.add(ingredientsMapper.mapToDto(recipeIngredient));
            }

            RecipeResponse recipeResponse = getRecipeResponse(recipeDto, ingredientDtos);
            recipeResponses.add(recipeResponse);
        }

        return recipeResponses;
    }

    public List<RecipeDto> getAllRecipeDtos() {
        List<Recipe> recipes = recipeRepository.findAll();
        System.out.println("Found " + recipes.size() + " recipes in the database");
        List<RecipeDto> recipeDtos = recipes.stream()
                .map(recipeMapper::mapToDto)
                .collect(Collectors.toList());
        return recipeDtos;
    }
}
