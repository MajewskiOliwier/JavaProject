package com.example.JavaProject.service.implementation;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.*;
import com.example.JavaProject.exception.ProfileHiddenException;
import com.example.JavaProject.mapper.UserMapper;
import com.example.JavaProject.repository.RoleRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.service.interfaces.AccountManagementService;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountManagementServiceImpl implements AccountManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Override
    public String promote(long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser == null){
            return "User with id"+id+" doesn't exist";
        }

        User user = foundUser.get();

        if(authenticationService.getCurrentUserId() == id){
            return "User cannot promote oneself";
        }

        if(user.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")){
            return "User with id"+id+" already is an admin";
        }


        Role userAdminRole = roleRepository.findByName("ROLE_ADMIN");
        if(userAdminRole == null){
            userAdminRole = new Role();
            userAdminRole.setName("ROLE_ADMIN");
            roleRepository.save(userAdminRole);
        }

        user.setRole(userAdminRole);
        userRepository.save(user);
        return "USER WITH ID "+id+" SUCCESSFULY PROMOTED TO ADMIN";
    }

    @Override
    @Transactional
    public RegisterDto updateAccount(RegisterDto registerDto) {
        Long userId = authenticationService.getCurrentUserId();

        Optional<User> updatedUser = userRepository.findById(userId);
        if (updatedUser.isEmpty()) {
            throw new RuntimeException("No user found with currently logged account.");
        }

        User user = updatedUser.get();

        if(user.isHidden()){
            throw new ProfileHiddenException("Profile has been deleted");
        }

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
        Long userId = authenticationService.getCurrentUserId();

        Optional<User> updatedUser = userRepository.findById(userId);
        if (updatedUser.isEmpty()) {
            throw new RuntimeException("No user found with currently logged account.");
        }

        User user = updatedUser.get();

        if(user.isHidden()){
            return "User has already been deleted";
        }
        user.setHidden(true);

        userRepository.save(user);

        return "User has been successfully deleted";
    }

    @Override
    public String getInfoByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                        () -> new RuntimeException("User not found with email: " + email));
        if (user.isHidden())
            throw new ProfileHiddenException("Profile is hidden");

        return "User profile is visible";
    }

    @Override
    public String hideAccount(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isHidden()) {
            return "User's account is already hidden.";
        }
        user.setHidden(true);
        userRepository.save(user);
        return "User account has been successfully hidden.";
    }
}
