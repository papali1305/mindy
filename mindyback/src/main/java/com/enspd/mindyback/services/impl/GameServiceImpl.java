package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.*;
import com.enspd.mindyback.exception.EntityNotFoundException;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.exception.InvalidOperationException;
import com.enspd.mindyback.models.*;
import com.enspd.mindyback.models.type.CompetenceType;
import com.enspd.mindyback.repository.GameRepository;
import com.enspd.mindyback.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private GameRepository gameRepository;

    @Autowired
    private SentenceCompletionService sentenceCompletionService;
    @Autowired
    private SentenceQcmService sentenceQcmService;

    @Autowired
    private EvaluationService evaluationService;

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
                evaluationService.createEvalutation(LeconDto.toEntity(leconDto));

                return games;
            case VERBAL_CONV:
                List<SentenceCompletion> sentenceCompletions = sentenceCompletionService.createSentenceCompletions(LeconDto.toEntity(leconDto), jwt);
                List<SentenceCompletionDto> sentenceCompletionsDto = new ArrayList<>();
                for (SentenceCompletion sentenceCompletion : sentenceCompletions) {
                    SentenceCompletionDto sentenceCompletionDto = SentenceCompletionDto.fromEntity(sentenceCompletion);
                    sentenceCompletionsDto.add(sentenceCompletionDto);
                }
                List<SentenceQcm> sentenceQcms = sentenceQcmService.createSentenceQcms(LeconDto.toEntity(leconDto), jwt);
                List<SentenceQcmDto> sentenceQcmsDto = new ArrayList<>();
                for (SentenceQcm sentenceQcm : sentenceQcms) {
                    SentenceQcmDto sentenceQcmDto = SentenceQcmDto.fromEntity(sentenceQcm);
                    sentenceQcmsDto.add(sentenceQcmDto);
                }
                games.addAll(sentenceCompletionsDto);
                games.addAll(sentenceQcmsDto);
                evaluationService.createEvalutation(LeconDto.toEntity(leconDto));

                return games;
            case SOCIAL:
                List<Communication> communications = communicationService.createCommunications(LeconDto.toEntity(leconDto), jwt);
                List<CommunicationDto> communicationsDto = new ArrayList<>();
                for (Communication communication : communications) {
                    CommunicationDto communicationDto = CommunicationDto.fromEntity(communication);
                    communicationsDto.add(communicationDto);
                }
                games.addAll(communicationsDto);
                evaluationService.createEvalutation(LeconDto.toEntity(leconDto));

                return games;
            default:
                throw new InvalidOperationException("Competence type inconnu", ErrorCodes.UNKNOWN_COMPETENCE_TYPE);
        }
    }

    @Override
    public LeconDto findLeconByGameId(Integer gameId) {
        Lecon lecon = gameRepository.findLeconByGameId(gameId).orElseThrow(() -> new EntityNotFoundException("Aucune lecon trouve avec l id " + gameId, ErrorCodes.LECON_NOT_FOUND));
        return LeconDto.fromEntity(lecon);
    }

    @Override
    public void validateLeconGame(Integer gameId) {
        gameRepository.validateLeconGame(gameId);
    }
}
