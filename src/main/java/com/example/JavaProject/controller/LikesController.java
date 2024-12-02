package com.example.JavaProject.controller;

import com.example.JavaProject.service.interfaces.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    private record LikesCountResponse(long likes) {}

    @GetMapping("/{id}/likes")
    public ResponseEntity<LikesCountResponse> getRecipeLikeCount(@PathVariable long id) {

        var likesObj = new LikesCountResponse(likesService.getRecipeLikes(id));
        return new ResponseEntity<>(likesObj, HttpStatus.OK);
    }

    @PutMapping("/{id}/likes")
    public ResponseEntity<String> addRecipeLike(@PathVariable long id) {
        return new ResponseEntity<>(likesService.likeRecipe(id), HttpStatus.OK);
    }
}