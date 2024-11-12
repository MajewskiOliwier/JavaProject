package com.example.JavaProject.service.implementation;

import com.example.JavaProject.entity.Role;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.repository.RoleRepository;
import com.example.JavaProject.repository.UserRepository;
import com.example.JavaProject.service.interfaces.AccountManagementService;
import com.example.JavaProject.service.interfaces.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AccountManagementServiceImpl implements AccountManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private AuthenticationService authenticationService;


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

        if(user.getRole().getName() == "ADMIN"){
            return "User with id"+id+" already is an admin";
        }


        Role userAdminRole = roleRepository.findByName("ADMIN");
        if(userAdminRole == null){
            userAdminRole = new Role();
            userAdminRole.setName("ADMIN");
            roleRepository.save(userAdminRole);
        }

        user.setRole(userAdminRole);
        userRepository.save(user);
        return "USER WITH ID "+id+" SUCCESSFULY PROMOTED TO ADMIN";
    }
}
