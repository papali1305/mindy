package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.GameDto;
import com.enspd.mindyback.dto.LeconDto;

import java.util.List;

public interface GameService {
    List<GameDto> createLeconGames(Integer leconId, String jwt);

    LeconDto findLeconByGameId(Integer gameId);
}
