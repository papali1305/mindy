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
public class Scenario extends Game {


    @Column
    private String aiQuestion;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "scenario")
    private ScenarioScene scenarioScene;

    @Column
    @Enumerated(EnumType.STRING)
    private ScenarioType scenarioType;


}
