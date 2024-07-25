package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

    @Query("SELECT c FROM Chapter c WHERE c.competence.id = :idCompetence AND c.isCurrent = true")
    List<Chapter> findAllCurrentByCompetenceId(@Param("idCompetence") Integer idCompetence);

    @Query("SELECT c FROM Chapter c WHERE c.competence.id = :idCompetence")
    Optional<List<Chapter>> findAllByCompetenceId(@Param("idCompetence") Integer idCompetence);
}
