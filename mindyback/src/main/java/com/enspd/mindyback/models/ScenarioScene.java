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
public class ScenarioScene extends AbstractEntity {

    @Column
    private String prompt;

    @Column
    private String pathToScene;

    @OneToOne
    @JoinColumn(name = "id_scenario", nullable = false)
    private Scenario scenario;
}
