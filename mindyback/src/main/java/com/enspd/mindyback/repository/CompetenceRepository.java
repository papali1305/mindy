package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository

public interface CompetenceRepository extends JpaRepository<Competence,Integer> {

    Optional<List<Competence>> findAllByUserId(@Param("idUser") Integer idUser);
}
