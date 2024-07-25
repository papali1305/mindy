package com.enspd.mindyback.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column()
    private String pathToScene;

    @OneToOne
    @JoinColumn(name = "scenarioId", nullable = false)
    @JsonBackReference
    private Scenario scenario;
}
