package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
}
