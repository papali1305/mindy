package com.enspd.mindyback.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Objectif extends AbstractEntity{

    @Column
    private String nom;

    @Column
    private String description;

    @Column
    private int priority;

    @ManyToOne
    @JoinColumn(name = "id_competence")
    private Competence competence;


}
