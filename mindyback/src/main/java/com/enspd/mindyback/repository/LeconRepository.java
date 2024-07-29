package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Lecon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface LeconRepository extends JpaRepository<Lecon, Integer> {
    Optional<List<Lecon>> findByChapterId(Integer chapterId);

    @Query("SELECT c.competenceType FROM Competence c JOIN c.chapters ch JOIN ch.lecons l WHERE l.id = :leconId")
    Optional<String> findCompetenceTypeByLeconId(@Param("leconId") Integer leconId);
}
