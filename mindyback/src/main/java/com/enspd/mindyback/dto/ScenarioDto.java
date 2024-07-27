package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.models.type.GameType;
import com.enspd.mindyback.models.type.ScenarioType;

import java.time.Instant;

public record ScenarioDto(Integer id, Instant creationDate, Instant lastModifiedDate, String name, String description,
                          boolean isPassed, String userResponse,
                          GameType type,
                          LeconDto leconDto
        , String aiQuestion, ScenarioType scenarioType) implements GameDto {


    @Override
    public Game toEntity() {
        Scenario game = new Scenario();
        game.setId(this.id);
        game.setCreationDate(this.creationDate);
        game.setLastModifiedDate(this.lastModifiedDate);
        game.setName(this.name);
        game.setDescription(this.description);
        game.setPassed(this.isPassed);
        game.setUserResponse(this.userResponse);
        game.setType(this.type);
        game.setLecon(LeconDto.toEntity(this.leconDto));
        game.setAiQuestion(this.aiQuestion);
        game.setScenarioType(this.scenarioType);
        return game;
    }

    public static ScenarioDto fromEntity(Scenario game) {
        return new ScenarioDto(game.getId(), game.getCreationDate(), game.getLastModifiedDate(), game.getName(), game.getDescription()
                , game.isPassed(), game.getUserResponse(), game.getType(), LeconDto.fromEntity(game.getLecon()),
                game.getAiQuestion(), game.getScenarioType());
    }
}
