package com.example.JavaProject.controller;

import com.example.JavaProject.response.IngredientResponse;
import com.example.JavaProject.service.interfaces.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    public ResponseEntity<List<IngredientResponse>> getAllIngredients(
                                                        @RequestParam(defaultValue = "") String phrase) {
        return new ResponseEntity<>(ingredientService.getIngredients(phrase), HttpStatus.OK);
    }
}
