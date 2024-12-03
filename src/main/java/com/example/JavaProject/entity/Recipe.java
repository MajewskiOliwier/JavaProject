package com.example.JavaProject.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.List;
import java.time.LocalDateTime;


@Entity
@Table(name = "recipe")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String recipeName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "preparationTime")
    private int preparationTime;


    private int difficulty;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "recipe_likes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    private List<User> likedby;

    @ManyToMany
    @JoinTable(
            name = "recipe_favourites",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    private List<User> favouritedBy;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeIngredients;

    @ElementCollection
    @CollectionTable(name = "recipe_procedures", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "recipeProcedures")
    private List<String> recipeProcedures;
}