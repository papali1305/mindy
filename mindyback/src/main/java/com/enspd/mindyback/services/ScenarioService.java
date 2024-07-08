package com.enspd.mindyback.services;

import com.enspd.mindyback.models.Scenario;


public interface ScenarioService {

    Scenario createScenario(Scenario scenario);
    Scenario findScenario(Integer idScenario);

    void deleteScenario(Integer idScenario);

    Scenario updateScenario(Scenario scenario);
}
