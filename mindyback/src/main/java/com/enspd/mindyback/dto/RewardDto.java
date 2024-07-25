package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Reward;

import java.time.Instant;

public record RewardDto(
        Integer id,
        Instant creationDate,
        Instant lastModifiedDate,
        UserDto userDto,
        int gems,
        int achievements,
        int experiencePoints,
        int level
) {

  public static RewardDto fromEntity(Reward reward) {
    return new RewardDto(
           reward.getId(),
           reward.getCreationDate(),
           reward.getLastModifiedDate(),
           UserDto.fromEntity(reward.getUser()),
           reward.getGems(),
           reward.getAchievements(),
           reward.getExperiencePoints(),
           reward.getLevel()
    );
  }

  public static Reward toEntity(RewardDto dto) {
      Reward reward = new Reward();

      reward.setId(dto.id());

      reward.setCreationDate(dto.creationDate());
      reward.setLastModifiedDate(dto.lastModifiedDate());
      reward.setUser(UserDto.toEntity(dto.userDto()));
      reward.setGems(dto.gems());
      reward.setAchievements(dto.achievements());
      reward.setExperiencePoints(dto.experiencePoints());
      reward.setLevel(dto.level());
      return reward;
  }
}
