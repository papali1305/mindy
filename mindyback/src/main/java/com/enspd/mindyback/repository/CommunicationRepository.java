package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Evaluation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface CommunicationRepository extends JpaRepository<Communication,Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Communication a SET a.isPassed = true WHERE a.id = :id")
    void validateCommunication(@Param("id") Integer id);
}
