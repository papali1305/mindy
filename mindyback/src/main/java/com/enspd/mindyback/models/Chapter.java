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
    private String name;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String description;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String objectives;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Lecon> lecons;

    @Column
    private boolean isCurrent = false;

    @Column
    private boolean isCompleted = false;

    @ManyToOne
    @JoinColumn(name = "competenceId")
    private Competence competence;

}
