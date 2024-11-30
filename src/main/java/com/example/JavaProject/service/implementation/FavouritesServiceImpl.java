package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.IngredientDto;
import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.RecipeIngredient;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.exception.ProfileHiddenException;
import com.example.JavaProject.mapper.IngredientsMapper;
import com.example.JavaProject.mapper.RecipeMapper;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.response.RecipeResponse;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import com.example.JavaProject.service.interfaces.FavouritesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouritesServiceImpl implements FavouritesService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientsMapper ingredientsMapper;
    private final AuthenticationService authenticationService;

    @Transactional
    @Override
    public String addToFavourite(long id) {
        Long userID = authenticationService.getCurrentUserId();

        if (userID == null) {
            return "User not logged in";
        }

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (recipe.getUser().isHidden()) {
            return "User has been deleted";
        }

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        boolean isAlreadyFavoured = recipe.getFavouritedBy().stream()
                .anyMatch(favUser -> favUser.getId().equals(userID));

        if (isAlreadyFavoured) {
            return "User has already added this recipe to favourites";
        }

        recipe.getFavouritedBy().add(user);
        recipeRepository.save(recipe);

        return "Recipe added to favourites successfully";
    }

    @Override
    public List<RecipeResponse> getFavouriteRecipes() {
        Long userID = authenticationService.getCurrentUserId();
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("No user found with currently logged account."));

        if (user.isHidden()) {
            throw new ProfileHiddenException("Profile has been deleted");
        }
        List<RecipeResponse> recipeResponses = new ArrayList<>();
        for (Recipe recipe : user.getFavourites()) {
            if (recipe.getUser().isHidden()) {
                continue;
            }
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
            recipeResponse.setAuthor(recipeDto.getAuthor());
            recipeResponse.setLikes(recipeDto.getLikes());
            recipeResponses.add(recipeResponse);
        }

        return recipeResponses;
    }

    @Transactional
    @Override
    public String deleteFavouriteRecipe(Long id) {
        Long userID = authenticationService.getCurrentUserId();
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("No user found with currently logged account."));
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        if (!user.getFavourites().contains(recipe)) {
            return "Recipe is not in favourites.";
        }

        user.getFavourites().remove(recipe);
        userRepository.save(user);
        return "Recipe removed from favourites successfully";
    }
}
