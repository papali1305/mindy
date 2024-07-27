package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.models.type.CommunicationType;
import com.enspd.mindyback.models.type.GameType;

import java.time.Instant;

public record CommunicationDto(Integer id, Instant creationDate, Instant lastModifiedDate, String name,
                               String description,
                               boolean isPassed, String userResponse,
                               GameType type,
                               LeconDto leconDto, String aiConv,
                               CommunicationType communicationType) implements GameDto {


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
        game.setLecon(LeconDto.toEntity(this.leconDto));
        game.setAiConv(this.aiConv);
        game.setCommunicationType(this.communicationType);
        return game;
    }

    public static CommunicationDto fromEntity(Communication game) {
        return new CommunicationDto(game.getId(), game.getCreationDate(), game.getLastModifiedDate(), game.getName(), game.getDescription()
                , game.isPassed(), game.getUserResponse(), game.getType(), LeconDto.fromEntity(game.getLecon()),
                game.getAiConv(), game.getCommunicationType());
    }
}