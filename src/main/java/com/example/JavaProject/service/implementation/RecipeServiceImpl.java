package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.IngredientDto;
import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.RecipeIngredient;
import com.example.JavaProject.mapper.IngredientsMapper;
import com.example.JavaProject.mapper.RecipeMapper;
import com.example.JavaProject.repository.IngredientRepository;
import com.example.JavaProject.repository.RecipeIngredientRepository;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.response.RecipeResponse;
import com.example.JavaProject.service.interfaces.RecipeService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private RecipeIngredientRepository recipeIngredientRepository;

    private RecipeMapper recipeMapper;
    private IngredientsMapper ingredientsMapper;
    private EntityManager entityManager;

    @Override
    public List<RecipeResponse> getAllRecipes(){
        // find alternative for findAll?
        List<Recipe> recipes= recipeRepository.findAll();

        List<RecipeResponse> recipeResponses = new ArrayList<>();
        for (Recipe recipe : recipes) {
            RecipeDto recipeDto = recipeMapper.mapToDto(recipe);
            RecipeResponse recipeResponse = new RecipeResponse();

            List<IngredientDto> ingredientDtos = new ArrayList<>();
            for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
                ingredientDtos.add(ingredientsMapper.mapToDto(recipeIngredient));
            }

            recipeResponse.setIngredients(ingredientDtos);
            recipeResponse.setRecipeName(recipeDto.getRecipeName());
            recipeResponse.setPreparationTime(recipeDto.getPreparationTime());
            recipeResponse.setDifficulty(recipeDto.getDifficulty());
            recipeResponses.add(recipeResponse);
        }

        return recipeResponses;
    }



    @Override
    public RecipeResponse getRecipe(long id){
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if(recipe.isEmpty()) {
//           throw Exception("custome Exception");
        }
        Recipe foundRecipe = recipe.get();

        RecipeDto recipeDto = recipeMapper.mapToDto(foundRecipe);


        List<IngredientDto> ingredientDtos = recipeDto.getIngredients();
        return new RecipeResponse(
                recipeDto.getRecipeName(),
                recipeDto.getPreparationTime(),
                recipeDto.getDifficulty(),
                ingredientDtos
        );
    }

    @Override
    public String addRecipe(RecipeDto recipeDto) {
        Recipe recipe = recipeMapper.mapToEntity(recipeDto);
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

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
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if (recipe.isEmpty()) {
            return "Recipe with given id doesn't exist";
        }

        Recipe updatedRecipe = recipe.get();
        updatedRecipe.setRecipeName(recipeDto.getRecipeName());
        updatedRecipe.setDifficulty(recipeDto.getDifficulty());
        updatedRecipe.setPreparationTime(recipeDto.getPreparationTime());

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findByIngredientNameAndMeasurement(ingredientDto.getIngredientName(), ingredientDto.getMeasurement());

            if (ingredient == null) {
                ingredient = ingredientRepository.save(ingredientsMapper.mapToEntity(ingredientDto));
            }

            if (ingredient == null) {
                return "Ingredient could not be saved";
            }

            Ingredient finalIngredient = ingredient;
            Optional<RecipeIngredient> existingRecipeIngredient = updatedRecipe.getRecipeIngredients()
                    .stream()
                    .filter(ri -> ri.getIngredient().equals(finalIngredient))
                    .findFirst();

            if (existingRecipeIngredient.isPresent()) {
                RecipeIngredient recipeIngredient = existingRecipeIngredient.get();
                recipeIngredient.setQuantity(ingredientDto.getQuantity());
                recipeIngredients.add(recipeIngredient);
            } else {
                RecipeIngredient newRecipeIngredient = new RecipeIngredient();
                newRecipeIngredient.setRecipe(updatedRecipe);
                newRecipeIngredient.setIngredient(ingredient);
                newRecipeIngredient.setQuantity(ingredientDto.getQuantity());
                recipeIngredients.add(newRecipeIngredient);
            }
        }

        updatedRecipe.setRecipeIngredients(recipeIngredients);

        recipeIngredientRepository.saveAll(recipeIngredients);

        entityManager.flush();
        recipeRepository.save(updatedRecipe);

        return "Recipe is modified";
    }
}
