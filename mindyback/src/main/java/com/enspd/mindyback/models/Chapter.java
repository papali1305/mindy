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
public class Chapter extends AbstractEntity{

    @Column
    private String nom;

    @Column
    private String description;

    @Column
    private String objSpec;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Lecon> lecons;

    @OneToOne
    @JoinColumn(name = "id_competence", nullable = false)
    private Competence competence;

}
