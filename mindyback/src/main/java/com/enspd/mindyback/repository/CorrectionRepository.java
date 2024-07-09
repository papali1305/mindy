package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Correction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrectionRepository extends JpaRepository<Correction, Integer> {
}
