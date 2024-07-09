package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Lecon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface LeconRepository extends JpaRepository<Lecon,Integer> {
}
