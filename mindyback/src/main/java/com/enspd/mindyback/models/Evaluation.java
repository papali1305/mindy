package com.enspd.mindyback.models;

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
public class Evaluation extends AbstractEntity{

    @Column
    private int evaluationNumber;

    @Column
    private String commentaire;

    @Column
    private int progres;

    @ManyToOne
    @JoinColumn(name = "id_lecon")
    private Lecon lecon;
}
