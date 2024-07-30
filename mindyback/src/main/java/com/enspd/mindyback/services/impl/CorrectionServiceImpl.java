package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.*;
import com.enspd.mindyback.models.Correction;
import com.enspd.mindyback.models.Evaluation;
import com.enspd.mindyback.repository.CorrectionRepository;
import com.enspd.mindyback.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorrectionServiceImpl implements CorrectionService {

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private LeconService leconService;

    @Autowired
    private IaService iaService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private RewardService rewardService;


    @Override
    @Transactional
    public CorrectionDto createCorrection(Integer gameId, GameResponseDto userResponse, String jwt) {

        Correction correction = iaService.corrigeGame(gameId, userResponse);
        LeconDto leconDto = gameService.findLeconByGameId(gameId);
        Evaluation evaluation = evaluationService.findLastEvaluation(leconDto.id());
        if (correction.isCorrect()) {
            evaluation.setNote(evaluation.getNote() + 1);
            gameService.validateLeconGame(gameId);
        }
        if (evaluation.getNote() == 6 && !leconDto.isPassed()) {
            leconService.validateLecon(leconDto.id());
            UserDto user = userService.findUserByJwt(jwt);
            RewardDto reward = rewardService.findRewardByUserId(user.id());
            rewardService.addExperiencePoints(user.id(), 120);
            rewardService.addGems(user.id(), 4);
            if (reward.experiencePoints() >= 120) {
                rewardService.addLevel(user.id(), 1);
                rewardService.reinitExperiencePoints(user.id(), 120);
            }
        }

        return CorrectionDto.fromEntity(correctionRepository.save(correction));
    }

    @Override
    public Correction findCorrection(Integer id) {

        return correctionRepository.findById(id).orElseThrow(() -> new RuntimeException("Aucune correction trouve avec l id " + id));
    }

    @Override
    public void deleteCorrection(Integer id) {
        correctionRepository.deleteById(id);
    }
}
