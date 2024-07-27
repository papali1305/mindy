package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.*;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.exception.InvalidOperationException;
import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.models.type.CompetenceType;
import com.enspd.mindyback.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private UserService userService;

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private LeconService leconService;

    @Autowired
    private CompetenceService competenceService;
    @Autowired
    private IaService iaService;

    @Override
    public List<GameDto> createLeconGames(Integer leconId, String jwt) {
        CompetenceType competenceType = leconService.findLeconCompetenceType(leconId);
        LeconDto leconDto = leconService.findLecon(leconId);
        UserDto userDto = userService.findUserByJwt(jwt);
        // Si verb -> compPhrase + Relier mot
        // Si non verb -> scenario Qro + scenario Qcm
        // Si social -> communication + communication Qcm

        List<GameDto> games = new ArrayList<>();

        switch (competenceType) {
            case NO_VERBAL_CONV:
                List<Scenario> scenarios = scenarioService.createScenarios(LeconDto.toEntity(leconDto), jwt);
                List<ScenarioDto> scenariosDto = new ArrayList<>();
                for (Scenario scenario : scenarios) {
                    ScenarioDto scenarioDto = ScenarioDto.fromEntity(scenario);
                    scenariosDto.add(scenarioDto);
                }
                games.addAll(scenariosDto);

                return games;
            case VERBAL_CONV:
                return null;
            case SOCIAL:
                List<Communication> communications = communicationService.createCommunications(LeconDto.toEntity(leconDto), jwt);
                List<CommunicationDto> communicationsDto = new ArrayList<>();
                for (Communication communication : communications) {
                    CommunicationDto communicationDto = CommunicationDto.fromEntity(communication);
                    communicationsDto.add(communicationDto);
                }
                games.addAll(communicationsDto);

                return games;
            default:
                throw new InvalidOperationException("Competence type inconnu", ErrorCodes.UNKNOWN_COMPETENCE_TYPE);
        }
    }
}
