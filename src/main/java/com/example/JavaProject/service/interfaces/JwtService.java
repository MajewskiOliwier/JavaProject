package com.example.JavaProject.service.interfaces;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface JwtService {
    public String generateToken(com.example.JavaProject.entity.User user);
    public Claims validateToken(String token);

}
