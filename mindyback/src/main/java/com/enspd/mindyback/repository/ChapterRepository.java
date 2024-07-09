package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Objectif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
}
