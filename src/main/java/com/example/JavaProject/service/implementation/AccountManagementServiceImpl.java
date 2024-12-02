package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.*;
import com.example.JavaProject.exception.ProfileHiddenException;
import com.example.JavaProject.exception.UserNotFoundException;
import com.example.JavaProject.mapper.UserMapper;
import com.example.JavaProject.repository.RoleRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.service.interfaces.AccountManagementService;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountManagementServiceImpl implements AccountManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public RegisterDto updateAccount(RegisterDto registerDto) {
        User user = getUser();

        user.setUserName(registerDto.getUsername());
        user.setAge(registerDto.getAge());
        user.setMan(registerDto.getIsMan());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);

        return userMapper.mapToDto(user);
    }

    @Override
    @Transactional
    public String deleteAccount(){
        User user = getUser();

        user.setHidden(true);
        userRepository.save(user);
        return "User has been successfully deleted";
    }

    @Override
    public String getInfoByEmail(String email) {
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("email", email));

        return user.isHidden() ? "User's account is hidden" : "User is visible";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String promote(long id) {
        if(authenticationService.getCurrentUserId() == id)
            throw new RuntimeException("User cannot promote oneself");

        String adminRole = "ROLE_ADMIN";
        User user = getUser(id);

        if(user.getRole().getName().equalsIgnoreCase(adminRole))
            throw new RuntimeException("User with id: "+id+" already is an admin");

        Role userAdminRole = roleRepository.findByName(adminRole);
        user.setRole(userAdminRole);
        userRepository.save(user);
        return "USER WITH ID "+id+" SUCCESSFULY PROMOTED TO ADMIN";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public String hideAccount(long id, boolean hidden) {
        User user = getUser(id);

        user.setHidden(hidden);
        userRepository.save(user);
        return hidden ?
                "User account has been successfully hidden." :
                "User account has been successfully unhidden.";
    }

    private User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private User getUser() {
        Long userId = authenticationService.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new PreAuthenticatedCredentialsNotFoundException("No user found with currently logged account."));

        if(user.isHidden()) throw new ProfileHiddenException();
        return user;
    }
}