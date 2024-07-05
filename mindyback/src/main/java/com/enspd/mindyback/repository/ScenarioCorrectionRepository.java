package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.ScenarioCorrection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioCorrectionRepository extends JpaRepository<ScenarioCorrection, Integer> {
}
