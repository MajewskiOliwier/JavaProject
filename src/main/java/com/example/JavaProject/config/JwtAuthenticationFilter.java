package com.example.JavaProject.config;

import com.example.JavaProject.entity.User;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.service.implementation.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            String email = jwtService.getUserEmailFromToken(token);
//
//            if (email != null) {
//                var userDetails = userDetailsService.loadUserByUsername(email);
//
//                if(SecurityContextHolder.getContext().getAuthentication() == null) {
//                    if (jwtService.validateToken(token) != null) {
//                        UsernamePasswordAuthenticationToken authToken =
//                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                        System.out.println("User authenticated: " + email);
//                    } else {
//                        System.out.println("Invalid JWT Token");
//                    }
//                }else{
//                    System.out.println("user is already authenticated");
//                }
//            } else {
//                System.out.println("Email is null");
//            }
//        } else {
//            System.out.println("No Authorization header or Bearer token found");
//        }
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract the token
            Long userId = jwtService.getUserIdFromToken(token); // Get user ID from token claims

            if (userId != null) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        true, // enabled
                        true, // accountNonExpired
                        true, // credentialsNonExpired
                        true, // accountNonLocked
                        user.getAuthorities()
                );





                // Check if the SecurityContext is empty (no authentication set)
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Validate the token
                    if (jwtService.validateToken(token) != null) {
                        // Create an authentication token and set it in the SecurityContext
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        System.out.println("Authorization header: " + authHeader);
                        System.out.println("Extracted token: " + token);
                        System.out.println("User ID from token: " + userId);

                    } else {
                        System.out.println("Invalid JWT Token");
                    }
                } else {
                    System.out.println("User is already authenticated");
                }
            } else {
                System.out.println("User ID is null");
            }
        } else {
            System.out.println("No Authorization header or Bearer token found");
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }



    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
