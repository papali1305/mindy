package com.enspd.mindyback.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table
@AllArgsConstructor
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Scenario extends AbstractEntity {

    private String scenarioName;

    private String scenarioDescription;

    private String aiQuestion;

    private ScenarioScene scenarioScene;

    private ScenarioType scenarioType;

    private boolean isPassed;

    private String userResponse;

    private ScenarioCorrection scenarioCorrection;

}
