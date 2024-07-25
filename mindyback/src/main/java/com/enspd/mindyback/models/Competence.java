package com.enspd.mindyback.models;

import com.enspd.mindyback.models.type.CompetenceType;
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



    public Competence(CompetenceType competenceType) {
        this.name = competenceType.getName();
        this.description = competenceType.getDescription();
        this.competenceType = competenceType;
    }

    @Column
    private String name;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String description;

    @Column
    private CompetenceType competenceType;

    @Column
    private float score = 0;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    @OneToMany(mappedBy = "competence", cascade = CascadeType.ALL)
    private List<Chapter> chapters;

}
