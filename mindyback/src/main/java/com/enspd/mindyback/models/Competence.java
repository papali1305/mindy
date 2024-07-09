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
public class Competence extends AbstractEntity {

    @Column
    private String nom;

    @Column
    private String description;

    @Column
    private CompetenceType competenceType;

    @OneToMany(mappedBy = "competence", cascade = CascadeType.ALL)
    private List<Objectif> objectifs;

    @OneToOne(mappedBy = "competence", cascade = CascadeType.ALL)
    private Chapter chapter;

}
