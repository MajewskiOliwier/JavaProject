package com.example.JavaProject.service.implementation;

import com.example.JavaProject.service.interfaces.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${auth.jwtSecret}")
    private String secretKey;

    @Value("${auth.jwtExpirationMs}")
    private long expirationTime;

    @Override
    public String extractEmail(String jwtToken){

        return extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractClaims(jwtToken);

        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String jwtToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Token is unsupported");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token is malformed");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token claims string is empty");
        }
    }

    private Key getSignKey(){
        return Keys.hmacShaKeyFor(
                        Decoders.BASE64.decode(secretKey));
    }

    @Override
    public String generateToken(Map<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public boolean isTokenValid(String jwtToken, UserDetails userDetails){
        String email = extractEmail(jwtToken);

        return email.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken){
        return extractClaims(jwtToken).getExpiration().before(new Date());
    }
}
