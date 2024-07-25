package com.enspd.mindyback.repository
        ;

import com.enspd.mindyback.models.Scenario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Scenario a SET a.isPassed = true WHERE a.id = :id")
    void validateScenario(@Param("id") Integer id);
}
