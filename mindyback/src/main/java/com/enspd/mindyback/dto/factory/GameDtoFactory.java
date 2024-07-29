package com.enspd.mindyback.dto.factory;

import com.enspd.mindyback.dto.CommunicationDto;
import com.enspd.mindyback.dto.GameDto;
import com.enspd.mindyback.dto.ScenarioDto;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.exception.InvalidOperationException;
import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.models.Scenario;

public class GameDtoFactory {

    public static GameDto createGameDto(Game game) {
        if (game instanceof Scenario) {
            return ScenarioDto.fromEntity((Scenario) game);
        } else if (game instanceof Communication) {
            return CommunicationDto.fromEntity((Communication) game);
        }
        throw new InvalidOperationException("Type de jeu inconnu" , ErrorCodes.UNKNOWN_GAME_TYPE);
    }

}
