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
public class Correction extends AbstractEntity {
    @Column
    private String analysis;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String response;

    @Column
    private boolean isCorrect;

    @OneToOne
    @JoinColumn(name = "gameId", nullable = false)
    private Game game;

}
