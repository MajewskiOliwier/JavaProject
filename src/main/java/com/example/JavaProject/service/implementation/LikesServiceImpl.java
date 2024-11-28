package com.example.JavaProject.service.implementation;

import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.exception.ProfileHiddenException;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.response.LikesCountResponse;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import com.example.JavaProject.service.interfaces.EmailService;
import com.example.JavaProject.service.interfaces.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.JavaProject.entity.Recipe;

import java.util.Optional;

import static java.nio.file.Files.isHidden;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final EmailService emailServiceImpl;

    @Override
    public LikesCountResponse getRecipeLikes(long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if (recipe.isEmpty()) {
            throw new RuntimeException("Recipe doesn't exist");
        }

        Recipe foundRecipe = recipe.get();
        if (isHidden(foundRecipe)) {
            throw new ProfileHiddenException("Profile has been deleted");
        }

        int likesCount = foundRecipe.getLikedby().size();
        return new LikesCountResponse(likesCount);
    }

    @Override
    @Transactional
    public String addLike(long id) {
        Long userID = authenticationService.getCurrentUserId();

        if (userID == null) {
            return "User not logged in";
        }

        Optional<Recipe> foundRecipe = recipeRepository.findById(id);
        if (foundRecipe.isEmpty()) {
            return "Recipe not found";
        }

        Recipe recipe = foundRecipe.get();

        boolean isAlreadyLiked = recipe.getLikedby().stream().anyMatch(user -> user.getId().equals(userID));
        if (isAlreadyLiked) {
            return "User already liked this recipe";
        }

        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User Not Found"));

        recipe.getLikedby().add(user);
        recipeRepository.save(recipe);

        emailServiceImpl.sendLikeNotification(recipe.getUser().getEmail());
        return "Recipe liked successfully";
    }

    @Override
    @Transactional
    public String removeLike(long id) {
        Long userID = authenticationService.getCurrentUserId();

        if (userID == null) {
            return "User not logged in";
        }

        Optional<Recipe> foundRecipe = recipeRepository.findById(id);
        if (foundRecipe.isEmpty()) {
            return "Recipe not found";
        }

        Recipe recipe = foundRecipe.get();

        boolean isLiked = recipe.getLikedby().stream().anyMatch(user -> user.getId().equals(userID));
        if (!isLiked) {
            return "User has not liked this recipe";
        }

        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User Not Found"));
        recipe.getLikedby().remove(user);
        recipeRepository.save(recipe);

        return "Recipe unliked successfully";
    }
}
