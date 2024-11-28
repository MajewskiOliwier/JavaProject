package com.example.JavaProject.service.interfaces;

import com.example.JavaProject.response.LikesCountResponse;

public interface LikesService {

    String addLike(long id);

    String removeLike(long id);

    LikesCountResponse getRecipeLikes(long id);
}
