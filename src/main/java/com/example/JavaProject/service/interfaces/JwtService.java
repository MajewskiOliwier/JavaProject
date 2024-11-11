package com.example.JavaProject.service.interfaces;

import io.jsonwebtoken.*;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface JwtService {
    Key getSignKey();

    public String generateToken(com.example.JavaProject.entity.User user);
    public Claims validateToken(String token);

    String getUserEmailFromToken(String token);

    Long getUserIdFromToken(String token);

    boolean isTokenExpired(String token);
}
