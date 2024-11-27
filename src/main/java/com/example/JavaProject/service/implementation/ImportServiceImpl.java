package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.repository.IngredientRepository;
import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.RecipeIngredient;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.service.interfaces.ImportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImportServiceImpl implements ImportService {

    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private static final Logger log = LoggerFactory.getLogger(ImportServiceImpl.class);


    @Autowired
    public ImportServiceImpl(ObjectMapper objectMapper, XmlMapper xmlMapper, RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.objectMapper = objectMapper;
        this.xmlMapper = xmlMapper;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional
    public List<RecipeDto> importRecipesFromJson(String json) throws IOException {
        List<RecipeDto> recipes = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, RecipeDto.class));

        // Logowanie przed zapisaniem danych
        recipes.forEach(recipeDto -> log.info("Importuję przepis: {}", recipeDto.getRecipeName()));

        List<Recipe> savedRecipes = recipes.stream()
                .map(recipeDto -> {
                    Recipe recipe = new Recipe();
                    recipe.setRecipeName(recipeDto.getRecipeName());
                    recipe.setPreparationTime(recipeDto.getPreparationTime());
                    recipe.setDifficulty(recipeDto.getDifficulty());
                    recipe.setCreatedAt(LocalDateTime.now());
                    recipe.setUpdatedAt(LocalDateTime.now());

                    // Zapisz składniki
                    List<RecipeIngredient> recipeIngredients = recipeDto.getIngredients().stream()
                            .map(ingredientDto -> {
                                Ingredient ingredient = ingredientRepository.findByIngredientName(ingredientDto.getIngredientName())
                                        .orElseGet(() -> {
                                            Ingredient newIngredient = new Ingredient();
                                            newIngredient.setIngredientName(ingredientDto.getIngredientName());
                                            return ingredientRepository.save(newIngredient);
                                        });

                                RecipeIngredient recipeIngredient = new RecipeIngredient();
                                recipeIngredient.setRecipe(recipe);
                                recipeIngredient.setIngredient(ingredient);
                                recipeIngredient.setQuantity(ingredientDto.getQuantity());
                                recipeIngredient.setMeasurement(ingredientDto.getMeasurement());

                                return recipeIngredient;
                            })
                            .collect(Collectors.toList());

                    recipe.setRecipeIngredients(recipeIngredients);

                    return recipe;
                })
                .collect(Collectors.toList());

        // Logowanie po zapisaniu danych do bazy
        List<Recipe> savedRecipesList = recipeRepository.saveAll(savedRecipes);
        savedRecipesList.forEach(recipe -> log.info("Przepis zapisany: {}", recipe.getRecipeName()));

        return savedRecipesList.stream()
                .map(recipe -> new RecipeDto(recipe))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDto> importRecipesFromXml(String xml) throws IOException {
        List<RecipeDto> recipes = xmlMapper.readValue(xml, xmlMapper.getTypeFactory().constructCollectionType(List.class, RecipeDto.class));

        // Logowanie przed zapisem
        recipes.forEach(recipeDto -> log.info("Importuję przepis: {}", recipeDto.getRecipeName()));

        List<Recipe> savedRecipes = recipes.stream()
                .map(recipeDto -> {
                    Recipe recipe = new Recipe();
                    recipe.setRecipeName(recipeDto.getRecipeName());
                    recipe.setPreparationTime(recipeDto.getPreparationTime());
                    recipe.setDifficulty(recipeDto.getDifficulty());
                    recipe.setCreatedAt(LocalDateTime.now());
                    recipe.setUpdatedAt(LocalDateTime.now());

                    // Zapisz składniki
                    List<RecipeIngredient> recipeIngredients = recipeDto.getIngredients().stream()
                            .map(ingredientDto -> {
                                Ingredient ingredient = ingredientRepository.findByIngredientName(ingredientDto.getIngredientName())
                                        .orElseGet(() -> {
                                            Ingredient newIngredient = new Ingredient();
                                            newIngredient.setIngredientName(ingredientDto.getIngredientName());
                                            return ingredientRepository.save(newIngredient);
                                        });

                                RecipeIngredient recipeIngredient = new RecipeIngredient();
                                recipeIngredient.setRecipe(recipe);
                                recipeIngredient.setIngredient(ingredient);
                                recipeIngredient.setQuantity(ingredientDto.getQuantity());
                                recipeIngredient.setMeasurement(ingredientDto.getMeasurement());

                                return recipeIngredient;
                            })
                            .collect(Collectors.toList());

                    recipe.setRecipeIngredients(recipeIngredients);

                    return recipe;
                })
                .collect(Collectors.toList());

        // Logowanie po zapisaniu danych do bazy
        List<Recipe> savedRecipesList = recipeRepository.saveAll(savedRecipes);
        savedRecipesList.forEach(recipe -> log.info("Przepis zapisany: {}", recipe.getRecipeName()));

        return savedRecipesList.stream()
                .map(recipe -> new RecipeDto(recipe))
                .collect(Collectors.toList());
    }
}