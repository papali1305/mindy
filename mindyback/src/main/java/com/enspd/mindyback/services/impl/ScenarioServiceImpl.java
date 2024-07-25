package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.repository.ScenarioRepository;
import com.enspd.mindyback.services.IaService;
import com.enspd.mindyback.services.ScenarioService;
import com.enspd.mindyback.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private UserService userService;

    @Override
    @Transactional

    public List<Scenario> createScenarios(Lecon lecon, String jwt) {
        UserDto userDto = userService.findUserByJwt(jwt);

        List<Scenario> scenarios = iaService.createScenarios(lecon, userDto);
        List<Scenario> scenariosToSend = new ArrayList<>();

        scenarios.forEach(scenario -> {
            scenario.setPassed(false);
            scenario.setLecon(lecon);
            scenariosToSend.add(scenarioRepository.save(scenario));
            // scenario.getScenarioScene().setScenario(null);

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
    @Transactional

    public Scenario updateScenario(Scenario scenario) {
        Scenario scenario1 = findScenario(scenario.getId());
        BeanUtils.copyProperties(scenario, scenario1, "id");
        return scenarioRepository.save(scenario1);
    }
}
