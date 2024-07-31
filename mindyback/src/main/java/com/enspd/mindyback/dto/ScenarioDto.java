package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.models.ScenarioScene;
import com.enspd.mindyback.models.type.GameType;
import com.enspd.mindyback.models.type.ScenarioType;

import java.time.Instant;

public record ScenarioDto(Integer id, Instant creationDate, Instant lastModifiedDate, String name, String description,
                          boolean isPassed, String userResponse,
                          GameType type,
                          CorrectionDto correction
        , String aiQuestion, ScenarioType scenarioType , ScenarioSceneDto scenarioScene) implements GameDto {


    public static ScenarioDto fromEntity(Scenario game) {
        return new ScenarioDto(game.getId(), game.getCreationDate(), game.getLastModifiedDate(), game.getName(), game.getDescription()
                , game.isPassed(), game.getUserResponse(), game.getType(),
                game.getCorrection() != null ? CorrectionDto.fromEntity(game.getCorrection()) : null
                ,
                game.getAiQuestion(), game.getScenarioType() , ScenarioSceneDto.fromEntity(game.getScenarioScene()));
    }

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
        game.setAiQuestion(this.aiQuestion);
        game.setScenarioType(this.scenarioType);
        if (this.correction != null) {
            game.setCorrection(CorrectionDto.toEntity(this.correction));
        }
        game.setScenarioScene(ScenarioSceneDto.toEntity(this.scenarioScene));
        return game;
    }
}
