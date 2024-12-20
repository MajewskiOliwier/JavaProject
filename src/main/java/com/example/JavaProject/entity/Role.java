package com.example.JavaProject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data

@Entity
@Table(name = "role")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;
}