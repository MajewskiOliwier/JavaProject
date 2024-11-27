package com.example.JavaProject.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportService {

    void importRecipesFromJson(MultipartFile file) throws IOException;
    void importRecipesFromXml(MultipartFile file) throws IOException;
}