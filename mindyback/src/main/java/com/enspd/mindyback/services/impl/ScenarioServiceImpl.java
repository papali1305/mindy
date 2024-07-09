package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.repository.ScenarioRepository;
import com.enspd.mindyback.services.IaService;
import com.enspd.mindyback.services.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScenarioServiceImpl implements ScenarioService {

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private IaService iaService;

    @Override
    public List<Scenario> createScenarios(Lecon lecon) {
        List<Scenario> scenarios = iaService.createScenarios(lecon);
        List<Scenario> scenariosToSend = new ArrayList<>();

        scenarios.forEach(scenario -> {
            scenario.setPassed(false);
            scenario.setLecon(lecon);
            scenariosToSend.add(scenarioRepository.save(scenario));

        });
        return scenariosToSend;
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
