package com.example.JavaProject.service.implementation;

import com.example.JavaProject.service.interfaces.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:application.properties")
public class JwtServiceImpl implements JwtService {
    @Value("${auth.jwtSecret}")
    private String jwtKey;

    @Value("${auth.jwtExpirationMs}")
    private long jwtExpirationTime;

    private Key getSignKey(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtKey));
    }

    public String generateToken(com.example.JavaProject.entity.User user){

        Map<String, Object> claims = new HashMap<>();
        claims.put("role",user.getRole().getName());

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("JWT validation error: " + e.getMessage());
        }
        return null;
    }



}
