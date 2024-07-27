package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.GameDto;

import java.util.List;

public interface GameService {
    List<GameDto> createLeconGames(Integer leconId, String jwt);
}
