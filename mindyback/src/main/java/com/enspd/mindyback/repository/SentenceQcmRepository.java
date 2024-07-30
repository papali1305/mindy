package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.SentenceCompletion;
import com.enspd.mindyback.models.SentenceQcm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentenceQcmRepository extends JpaRepository<SentenceQcm, Integer> {
}
