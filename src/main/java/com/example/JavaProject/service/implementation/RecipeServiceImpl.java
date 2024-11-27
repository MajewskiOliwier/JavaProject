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
import com.example.JavaProject.response.LikesCountResponse;
import com.example.JavaProject.response.RecipeResponse;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import com.example.JavaProject.service.interfaces.RecipeService;
import jakarta.persistence.EntityManager;
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
    private EntityManager entityManager;


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
    public RecipeResponse getRecipe(long id){
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if(recipe.isEmpty()) {
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
        recipeResponse.setRecipeName(recipeDto.getRecipeName());
        recipeResponse.setDifficulty(recipeDto.getDifficulty());
        recipeResponse.setPreparationTime(recipeDto.getPreparationTime());
        recipeResponse.setAuthor(recipeDto.getAuthor());
        recipeResponse.setLikes(recipeDto.getLikes());
        recipeResponse.setIngredients(ingredientDtos);
        return recipeResponse;
    }

    @Override
    public LikesCountResponse getRecipeLikes(long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if(recipe.isEmpty()) {
            throw new RuntimeException("Recipe doesn't exist");
        }

        Recipe foundRecipe = recipe.get();
        if(isHidden(foundRecipe)){
            throw new ProfileHiddenException("Profile has been deleted");
        }

        int likesCount =  (int)foundRecipe.getLikedby().stream().count();
        return new LikesCountResponse(
                likesCount
        );
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

        recipeRepository.save(recipe);
        recipeIngredientRepository.saveAll(recipeIngredients);

        return "Added new recipe";
    }




    @Transactional
    @Override
    public String modifyRecipe(long id, RecipeDto recipeDto) {
        Recipe updatedRecipe = recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("ID not found"));

        if(!isOwner(updatedRecipe)){
            return "User can only modify their recipe";
        }

        if(isHidden(updatedRecipe)){
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

    private static boolean isHidden(Recipe updatedRecipe) {
        return updatedRecipe.getUser().isHidden();
    }

    private static void UpdateRecipeData(RecipeDto recipeDto, Recipe updatedRecipe) {
        updatedRecipe.setRecipeName(recipeDto.getRecipeName());
        updatedRecipe.setDifficulty(recipeDto.getDifficulty());
        updatedRecipe.setPreparationTime(recipeDto.getPreparationTime());
    }

    private void AddNewIngredients(RecipeDto recipeDto, Recipe updatedRecipe) {
        for(IngredientDto ingredientDto : recipeDto.getIngredients()){
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
        for(RecipeIngredient recipeIngredient : existingIngredients){
            boolean unchangedIngredient =  recipeDto.getIngredients()
                    .stream()
                    .anyMatch(ingredientDto -> (ingredientDto.getIngredientName().equalsIgnoreCase(recipeIngredient.getIngredient().getIngredientName())
                    && ingredientDto.getMeasurement().equalsIgnoreCase(recipeIngredient.getIngredient().getMeasurement())));

            if(!unchangedIngredient){
                recipeIngredient.getRecipe().getRecipeIngredients().remove(recipeIngredient);
                recipeIngredient.getIngredient().getIngredientsRecipe().remove(recipeIngredient);
                recipeIngredient.setRecipe(null);
                recipeIngredient.setIngredient(null);

                recipeIngredientRepository.delete(recipeIngredient);
            }
        }
    }

    @Override
    public String addLike(long id) {
        Long userID = authenticationService.getCurrentUserId();

        if (userID == null) {
            return "User not logged in";
        }

        Optional<Recipe> foundRecipe = recipeRepository.findById(id);
        if(foundRecipe.isEmpty()){
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
        return "Recipe liked successfully ";
    }

    @Transactional
    @Override
    public String addToFavourite(long id) {
        Long userID = authenticationService.getCurrentUserId();

        if (userID == null) {
            return "User not logged in";
        }

        Optional<Recipe> foundRecipe = recipeRepository.findById(id);
        if (foundRecipe.isEmpty()) {
            return "Recipe not found";
        }

        Recipe recipe = foundRecipe.get();

        boolean isUserDeleted = recipe.getUser().isHidden();
        if (isUserDeleted) {
            return "User has been deleted";
        }

        boolean isAlreadyFavoured = recipe.getFavouritedBy().stream()
                .anyMatch(user -> user.getId().equals(userID));
        if (isAlreadyFavoured) {
            return "User has already added this recipe to favourite";
        }

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        recipe.getFavouritedBy().add(user);
        recipeRepository.save(recipe);
        return "Recipe added to favourites successfully ";
    }

    @Override
    public List<RecipeResponse> getFavouriteRecipes() {
        Long userID = authenticationService.getCurrentUserId();
        Optional<User> updatedUser = userRepository.findById(userID);
        if (updatedUser.isEmpty()) {
            throw new RuntimeException("No user found with currently logged account.");
        }

        User user = updatedUser.get();

        if(user.isHidden()){
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

    @Override
    public void saveAll(List<RecipeDto> recipes) {
        List<Recipe> recipeEntities = recipes.stream()
                .map(recipeMapper::mapToEntity)
                .collect(Collectors.toList());
        recipeRepository.saveAll(recipeEntities);
    }



}
