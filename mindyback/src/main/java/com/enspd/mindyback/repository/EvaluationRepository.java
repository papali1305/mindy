package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Evaluation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface EvaluationRepository extends JpaRepository<Evaluation,Integer> {
    Optional<List<Evaluation>> findAllByLeconIdOrderByCreationDate(Integer idRegime);

    }
