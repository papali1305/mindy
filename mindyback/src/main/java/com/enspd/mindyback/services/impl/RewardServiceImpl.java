package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.RewardDto;
import com.enspd.mindyback.exception.EntityNotFoundException;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.models.Reward;
import com.enspd.mindyback.repository.RewardRepository;
import com.enspd.mindyback.services.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private RewardRepository rewardRepository;

    @Override
    public RewardDto findRewardByUserId(Integer userId) {
        return rewardRepository.findByUserId(userId).map(RewardDto::fromEntity).orElseThrow(() -> new EntityNotFoundException("Aucune récompense trouvée pour l'utilisateur " + userId, ErrorCodes.REWARD_NOT_FOUND));
    }

    @Override
    public RewardDto createReward(RewardDto reward) {
        return RewardDto.fromEntity(rewardRepository.save(RewardDto.toEntity(reward)));
    }

    @Override
    public void addGems(Integer userId, int gems) {
        Reward reward = rewardRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Aucune récompense trouvée pour l'utilisateur " + userId, ErrorCodes.REWARD_NOT_FOUND));
        reward.setGems(reward.getGems() + gems);
        rewardRepository.save(reward);
    }

    @Override
    public void addAchievements(Integer userId, int achievements) {

    }

    @Override
    public void addExperiencePoints(Integer userId, int experiencePoints) {
        Reward reward = rewardRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Aucune récompense trouvée pour l'utilisateur " + userId, ErrorCodes.REWARD_NOT_FOUND));
        reward.setExperiencePoints(reward.getExperiencePoints() + experiencePoints);
        rewardRepository.save(reward);
    }

    @Override
    public  void reinitExperiencePoints(Integer userId, int experiencePoints) {
        Reward reward = rewardRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Aucune récompense trouvée pour l'utilisateur " + userId, ErrorCodes.REWARD_NOT_FOUND));
        reward.setExperiencePoints(reward.getExperiencePoints() - experiencePoints);
        rewardRepository.save(reward);
    }

    @Override
    public void addLevel(Integer userId, int level) {
        Reward reward = rewardRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Aucune récompense trouvée pour l'utilisateur " + userId, ErrorCodes.REWARD_NOT_FOUND));
        reward.setLevel(reward.getLevel() + level);
        rewardRepository.save(reward);
    }
}
