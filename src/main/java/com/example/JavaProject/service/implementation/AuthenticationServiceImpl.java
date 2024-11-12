package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.LoginDto;
import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.Role;
import com.example.JavaProject.exception.UserNotFoundException;
import com.example.JavaProject.repository.RoleRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.response.AuthenticationResponse;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import lombok.AllArgsConstructor;
import com.example.JavaProject.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtServiceImpl;

    @Override
    public String register(RegisterDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new RuntimeException(registerDto.getEmail()+" Email is taken");
        }

        Role userRole = roleRepository.findByName("USER");
        if(userRole == null){
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        var user = User.builder()
                .userName(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .role(userRole)
                .build();

        userRepository.save(user);
        return "USER REGISTRATION SUCCESSFUL";
    }

    @Override
    public AuthenticationResponse login(LoginDto loginDto) {
        try {
            User newUser = userRepository.findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(loginDto.getPassword(), newUser.getPassword())) {
                throw new RuntimeException("Invalid password");
            }


            String token = jwtServiceImpl.generateToken(newUser);

            return new AuthenticationResponse(token, newUser.getRole().getName());
        }catch (AuthenticationException e) {
            return new AuthenticationResponse(null, null);

        }
    }

    @Override
    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Principal: " + principal);

        // try to refactor the instanceOf
        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            return userRepository.findByEmail(email)
                    .map(User::getId)
                    .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
        }

        throw new UserNotFoundException("User is not authenticated");}
}
