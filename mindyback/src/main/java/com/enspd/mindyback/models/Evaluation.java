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
    private String comment;

    @Column
    private int progress;

    @Column
    private int note; // 0-10

    @ManyToOne
    @JoinColumn(name = "leconId")
    private Lecon lecon;
}
