package com.enspd.mindyback.repository
        ;

import com.enspd.mindyback.models.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {
}
