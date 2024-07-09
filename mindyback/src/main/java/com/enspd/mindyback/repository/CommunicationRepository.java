package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CommunicationRepository extends JpaRepository<Communication,Integer> {
}
