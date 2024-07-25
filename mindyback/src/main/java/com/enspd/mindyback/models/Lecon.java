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
public class Lecon extends AbstractEntity{

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String objectives;

    @Column
    private boolean isPassed;

    @OneToMany(mappedBy = "lecon", cascade = CascadeType.ALL)
    private List<Game> games;


    @OneToMany(mappedBy = "lecon", cascade = CascadeType.ALL)
    private List<Evaluation> evaluation;

    @ManyToOne
    @JoinColumn(name = "chapterId")
    private Chapter chapter;
}
