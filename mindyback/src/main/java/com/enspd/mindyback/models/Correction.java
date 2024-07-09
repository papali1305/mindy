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

    @Column
    private String response;

    @OneToOne
    @JoinColumn(name = "id_game", nullable = false)
    private Game game;

}
