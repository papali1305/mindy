package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.RewardDto;

public interface RewardService {
    RewardDto findRewardByUserId(Integer userId);

    RewardDto createReward(RewardDto rewardDto);

    void addGems(Integer userId, int gems);
    void addAchievements(Integer userId, int achievements);
    void addExperiencePoints(Integer userId, int experiencePoints);

    void reinitExperiencePoints(Integer userId, int experiencePoints);

    void addLevel(Integer userId, int level);

}
