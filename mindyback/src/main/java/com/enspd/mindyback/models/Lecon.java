package com.enspd.mindyback.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Lecon extends AbstractEntity{

    @Column
    private String nom;

    @Column
    private String description;

    @Column
    private String objSpec;

    @OneToMany(mappedBy = "lecon", cascade = CascadeType.ALL)
    private List<Scenario> scenarios;

    @OneToMany(mappedBy = "lecon", cascade = CascadeType.ALL)
    private List<Communication> communication;

    @OneToMany(mappedBy = "lecon", cascade = CascadeType.ALL)
    private List<Evaluation> evaluation;

    @ManyToOne
    @JoinColumn(name = "id_chapter")
    private Chapter chapter;
}
