package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Integer> {

    @Query("select r from Reward r where r.user.id = ?1")
    Optional<Reward> findByUserId(Integer userId);
}
