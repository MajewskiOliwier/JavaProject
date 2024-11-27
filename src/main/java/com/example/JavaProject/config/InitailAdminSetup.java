package com.example.JavaProject.config;

import com.example.JavaProject.entity.User;
import com.example.JavaProject.repository.RoleRepository;
import com.example.JavaProject.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import com.example.JavaProject.entity.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class InitailAdminSetup {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void setupAdmin() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        if(!userRepository.existsByEmail("admin@gmail.com")){
            User admin = User.builder()
                    .userName("Admin1")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("ZAQ!2wsx"))
                    .role(adminRole)
                    .age(23)
                    .isMan(true)
                    .isHidden(false)
                    .build();
            userRepository.save(admin);
        }
    }

    @PostConstruct
    public  void setupUserRole(){
        Role userRole = roleRepository.findByName("ROLE_USER");
        if(userRole == null){
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
    }
}
