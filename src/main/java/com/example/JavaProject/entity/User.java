package com.example.JavaProject.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.mapping.List;

import java.time.LocalDateTime;


@Entity
@Table(name = "recipe")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50)
    private String userName;

    private Integer age;


    private String email;

    @Min(8)
    @NotNull
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;



//    @OneToMany(mappedBy = "user")
//    private List<Recipe> recipes;



//    @ManyToMany
//    @JoinTable(name = "user_recipe",
//                joinColumns = @JoinColumn(name = "user_id"),
//                inverseJoinColumns = @JoinColumn(name = "recipe_id")
//    )
//    private List<Ingredient> likes;


}
