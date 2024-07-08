package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.repository.ScenarioRepository;
import com.enspd.mindyback.services.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScenarioServiceImpl implements ScenarioService {

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Override
    public Scenario createScenario(Scenario scenario) {
        return scenarioRepository.save(scenario);
    }

    @Override
    public Scenario findScenario(Integer idScenario) {
        return scenarioRepository.findById(idScenario).orElseThrow(() -> new RuntimeException("Aucun scenario trouve"));
    }

    @Override
    public void deleteScenario(Integer idScenario) {
        scenarioRepository.deleteById(idScenario);
    }

    @Override
    public Scenario updateScenario(Scenario scenario) {
        Scenario scenario1 = findScenario(scenario.getId());
        scenario.setId(scenario1.getId());
        scenario1 = scenario;
        return scenarioRepository.save(scenario1);
    }
}
