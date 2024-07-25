package com.enspd.mindyback.controllers;

import com.enspd.mindyback.controllers.api.GameApi;
import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.services.CommunicationService;
import com.enspd.mindyback.services.LeconService;
import com.enspd.mindyback.services.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.enspd.mindyback.config.Utils.GAME_ENDPOINT;


@RestController(GAME_ENDPOINT)
public class GameController implements GameApi {

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private LeconService leconService;

    @Override
    public List<Scenario> createLeconScenarioGames(Integer leconId, String jwt) {
        Lecon lecon = leconService.findLecon(leconId);
        List<Scenario> scenarios = scenarioService.createScenarios(lecon);
        for (Scenario scenario : scenarios) {
            scenario.setLecon(null);
        }
        return scenarios;
    }

    @Override
    public List<Communication> createLeconCommunicationGames(Integer leconId, String jwt) {
        Lecon lecon = leconService.findLecon(leconId);

        List<Communication> communications = communicationService.createCommunications(lecon);
        communications.forEach((communication -> {
            communication.setLecon(null);
        }));
        return communications;
    }


    @Override
    public Scenario findGameScenarioById(Integer id) {
        return scenarioService.findScenario(id);
    }

    @Override
    public Communication findGameCommunicationById(Integer id) {
        return communicationService.findCommunication(id);
    }

}
