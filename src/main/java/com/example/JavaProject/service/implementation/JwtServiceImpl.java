package com.example.JavaProject.service.implementation;

import com.example.JavaProject.service.interfaces.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
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

    @Override
    public Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtKey);
        System.out.println("Decoded Secret Key Length: " + keyBytes.length); // Add this for debugging
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(com.example.JavaProject.entity.User user){
        System.out.println("Generating token with secret key: " + Base64.getEncoder().encodeToString(getSignKey().getEncoded()));
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",user.getRole().getName());
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(user.getId()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    @Override
    public Claims validateToken(String token) {
        try {
            System.out.println("Validating token: " + token);
            System.out.println("Using secret key: " + Base64.getEncoder().encodeToString(getSignKey().getEncoded()));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("Parsed claims: " + claims);
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


//    @Override
//    public String getUserEmailFromToken(String token) {
//        Claims claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
//        return claims.getSubject();
//

    @Override
    public String getUserEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Print all claims for debugging
            System.out.println("JWT Claims: " + claims);

            return claims.getSubject(); // This should return the email if it's stored as the subject
        } catch (Exception e) {
            System.out.println("Error parsing JWT token: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String subject = claims.getSubject();
            System.out.println("Subject (User ID) extracted from token: " + subject);
            return Long.parseLong(subject);
        } catch (Exception e) {
            System.out.println("Error parsing JWT token: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}
