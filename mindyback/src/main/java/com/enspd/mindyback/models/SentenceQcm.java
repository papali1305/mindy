package com.enspd.mindyback.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
public class SentenceQcm extends Game {

    @Column
    private String sentenceToComplete;
    @Column
    private String words;
    @Column
    private String response;


}
