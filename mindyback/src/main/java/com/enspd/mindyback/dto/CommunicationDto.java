package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.models.type.CommunicationType;
import com.enspd.mindyback.models.type.GameType;

import java.time.Instant;

public record CommunicationDto(Integer id, Instant creationDate, Instant lastModifiedDate, String name,
                               String description,
                               boolean isPassed, String userResponse,
                               GameType type, String aiConv, CorrectionDto correction,
                               CommunicationType communicationType) implements GameDto {


    public static CommunicationDto fromEntity(Communication game) {
        return new CommunicationDto(game.getId(), game.getCreationDate(), game.getLastModifiedDate(), game.getName(), game.getDescription()
                , game.isPassed(), game.getUserResponse(), game.getType(),
                game.getAiConv(),
                game.getCorrection() == null ? null : CorrectionDto.fromEntity(game.getCorrection()), game.getCommunicationType());
    }

    @Override
    public Game toEntity() {
        Communication game = new Communication();
        game.setId(this.id);
        game.setCreationDate(this.creationDate);
        game.setLastModifiedDate(this.lastModifiedDate);
        game.setName(this.name);
        game.setDescription(this.description);
        game.setPassed(this.isPassed);
        game.setUserResponse(this.userResponse);
        game.setType(this.type);
        game.setAiConv(this.aiConv);
        game.setCommunicationType(this.communicationType);
        if (this.correction != null) {
            game.setCorrection(CorrectionDto.toEntity(this.correction));
        }
        return game;
    }
}