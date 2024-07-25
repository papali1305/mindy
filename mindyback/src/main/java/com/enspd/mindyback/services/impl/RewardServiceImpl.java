package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.RewardDto;
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
        return null;
    }

    @Override
    public RewardDto createReward(RewardDto reward) {
        return RewardDto.fromEntity(rewardRepository.save(RewardDto.toEntity(reward)));
    }

    @Override
    public void addGems(Integer userId, int gems) {

    }

    @Override
    public void addAchievements(Integer userId, int achievements) {

    }

    @Override
    public void addExperiencePoints(Integer userId, int experiencePoints) {

    }

    @Override
    public void addLevel(Integer userId, int level) {

    }
}
