package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.models.Lecon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface GameRepository extends JpaRepository<Game, Integer> {

     @Query("SELECT l FROM Game g JOIN g.lecon l WHERE g.id = :gameId")
     Optional<Lecon> findLeconByGameId(@Param("gameId") Integer gameId);
}
