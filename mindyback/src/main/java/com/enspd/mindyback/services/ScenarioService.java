package com.enspd.mindyback.services;

import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.Scenario;

import java.util.List;


public interface ScenarioService {

    List<Scenario> createScenarios(Lecon lecon);

    Scenario findScenario(Integer idScenario);

    void deleteScenario(Integer idScenario);

    Scenario updateScenario(Scenario scenario);
}
