package com.example.JavaProject.repository;

import com.example.JavaProject.dto.RegisterDto;
import com.example.JavaProject.entity.User;
import com.example.JavaProject.response.LikesCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByUserName(String userName);

    Optional<User> findByEmail(String email);
}
