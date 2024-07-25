package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Lecon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface LeconRepository extends JpaRepository<Lecon, Integer> {
    Optional<List<Lecon>> findByChapterId(Integer chapterId);
}
