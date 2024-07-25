package com.enspd.mindyback.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table
@AllArgsConstructor
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Reward extends AbstractEntity {



    @OneToOne(mappedBy = "reward")
    private User user;

    @Column(nullable = false)
    private int gems = 400; // Nombre de gemmes

    @Column(nullable = false)
    private int achievements = 0; // Nombre de succès

    @Column(nullable = false)
    private int experiencePoints = 0; // Points d'expérience

    @Column(nullable = false)
    private int level = 1; // Niveau du joueur
}
