package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.RecipeDto;
import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.exception.ProfileHiddenException;
import com.example.JavaProject.exception.RecipeNotFoundException;
import com.example.JavaProject.exception.UserNotFoundException;
import com.example.JavaProject.mapper.IngredientsMapper;
import com.example.JavaProject.mapper.RecipeMapper;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import com.example.JavaProject.service.interfaces.FavouritesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Result result = getResult(id);

        if (result.user().isHidden())
            throw new ProfileHiddenException();
        if(result.recipe().getFavouritedBy().contains(result.user()))
            throw new RuntimeException("User has already added this recipe to favourites");

        result.recipe().getFavouritedBy().add(result.user());
        recipeRepository.save(result.recipe());

        return "Recipe added to favourites successfully";
    }

    @Override
    public List<RecipeDto> getFavouriteRecipes() {
        Long userID = authenticationService.getCurrentUserId();
        User user = userRepository.findById(userID).orElseThrow(
                            () -> new UserNotFoundException(userID));

        if (user.isHidden()) throw new ProfileHiddenException();

        return user.getFavourites().stream()
                        .map(recipeMapper::mapToDto)
                        .toList();
    }

    @Transactional
    @Override
    public String deleteFavouriteRecipe(Long id) {
        Result result = getResult(id);

        if (!result.user().getFavourites().contains(result.recipe()))
            throw new RuntimeException("Recipe is not in favourites.");

        result.user().getFavourites().remove(result.recipe());
        userRepository.save(result.user());
        return "Recipe removed from favourites successfully";
    }

    private Result getResult(long id) {
        Long userID = authenticationService.getCurrentUserId();

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException(userID));
        return new Result(recipe, user);
    }

    private record Result(Recipe recipe, User user) {}
}