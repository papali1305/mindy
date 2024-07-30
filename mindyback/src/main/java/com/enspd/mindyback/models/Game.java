package com.enspd.mindyback.models;

import com.enspd.mindyback.models.type.GameType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public class Game extends AbstractEntity {

    @Column
    private String name;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column
    private GameType type;

    @ManyToOne
    @JoinColumn(name = "leconId")
    private Lecon lecon;


    @Column
    private boolean isPassed = false;

    @Column
    private String userResponse;


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "game")
    private Correction correction;

}
