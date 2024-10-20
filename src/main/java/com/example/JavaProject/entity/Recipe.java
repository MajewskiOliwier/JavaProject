package com.example.JavaProject.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.mapping.List;

import java.time.LocalDateTime;


@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50)
    private String recipeName;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "preparationTime")
    private int preparationTime;

    @Min(1)
    @Max(5)
    private int difficulty;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToMany(mappedBy = "likedRecipes")
//    private List<User> likes;

//    @ManyToMany
//    @JoinTable(name = "recipe_ingredient",
//                joinColumns = @JoinColumn(name = "recipe_id"),
//                inverseJoinColumns = @JoinColumn(name = "ingredient_id")
//    )
//    private List<Ingredient> ingredients;


}
