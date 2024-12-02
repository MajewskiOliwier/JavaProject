package com.example.JavaProject.service.implementation;

import com.example.JavaProject.entity.Recipe;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.exception.RecipeNotFoundException;
import com.example.JavaProject.exception.UserNotFoundException;
import com.example.JavaProject.repository.RecipeRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import com.example.JavaProject.service.interfaces.EmailService;
import com.example.JavaProject.service.interfaces.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final EmailService emailServiceImpl;

    @Override
    @Transactional
    public long getRecipeLikes(long id) {
        if(!recipeRepository.existsById(id))
                throw new RecipeNotFoundException(id);

        return recipeRepository.countLikesByRecipeId(id);
    }

    @Override
    @Transactional
    public String likeRecipe(long id) {
        Long userID = authenticationService.getCurrentUserId();
        User user = userRepository.findById(userID).orElseThrow(
                            () -> new UserNotFoundException(userID));

        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                            () -> new RecipeNotFoundException(id));

        if(recipe.getLikedby().contains(user)) {
            recipe.getLikedby().remove(user);
            recipeRepository.save(recipe);
            return "Recipe unliked successfully";
        } else {
            recipe.getLikedby().add(user);
            recipeRepository.save(recipe);
            emailServiceImpl.sendLikeNotification(recipe.getUser().getEmail());
            return "Recipe liked successfully";
        }
    }
}
