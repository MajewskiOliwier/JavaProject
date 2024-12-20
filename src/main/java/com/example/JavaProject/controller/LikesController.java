package com.example.JavaProject.controller;

import com.example.JavaProject.response.LikesCountResponse;
import com.example.JavaProject.service.interfaces.LikesService;
import com.example.JavaProject.service.interfaces.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/recipes/{id}/likes")
    public ResponseEntity<LikesCountResponse> getRecipeLikeCount(@PathVariable long id) {
        return new ResponseEntity<>(likesService.getRecipeLikes(id), HttpStatus.OK);
    }

    @PutMapping("/recipes/{id}/likes")
    public ResponseEntity<String> addRecipeLike(@PathVariable long id) {
        return new ResponseEntity<>(likesService.addLike(id), HttpStatus.OK);
    }

    @PutMapping("recipes/{id}/unlikes")
    public ResponseEntity<String> removeRecipeLike(@PathVariable long id) {
        return new ResponseEntity<>(likesService.removeLike(id), HttpStatus.OK);
    }

}
