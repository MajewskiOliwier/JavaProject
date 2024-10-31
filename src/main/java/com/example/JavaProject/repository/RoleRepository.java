package com.example.JavaProject.repository;

import com.example.JavaProject.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long>  {
    Role findByName(String name);
}