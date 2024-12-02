package com.example.JavaProject.service.implementation;

import com.example.JavaProject.entity.Ingredient;
import com.example.JavaProject.mapper.IngredientsMapper;
import com.example.JavaProject.repository.IngredientRepository;
import com.example.JavaProject.response.IngredientResponse;
import com.example.JavaProject.service.interfaces.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientsMapper ingredientsMapper;

    @Override
    public List<IngredientResponse> getIngredients(String phrase) {
        List<Ingredient> ingredients = ingredientRepository.findAllByIngredientNameContainingIgnoreCase(phrase);

        return ingredients.stream()
                .map(ingredientsMapper::mapToResponse)
                .toList();
    }
}
