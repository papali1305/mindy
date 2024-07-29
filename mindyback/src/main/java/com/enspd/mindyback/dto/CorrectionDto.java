package com.enspd.mindyback.dto;

import com.enspd.mindyback.dto.factory.GameDtoFactory;
import com.enspd.mindyback.models.Correction;
import com.enspd.mindyback.models.Game;

import java.time.Instant;

public record CorrectionDto(Integer id, Instant creationDate, Instant lastModifiedDate, String analysis, String response, boolean isCorrect, GameDto gameDto) {

    public static CorrectionDto fromEntity(Correction entity) {
        return new CorrectionDto(entity.getId(),
                entity.getCreationDate(),
                entity.getLastModifiedDate(),
                entity.getAnalysis(),
                entity.getResponse(),
                entity.isCorrect(),
                entity.getGame() != null ? GameDtoFactory.createGameDto(entity.getGame()): null);
    }

    public static Correction toEntity(CorrectionDto dto) {
        Correction entity = new Correction();
        entity.setId(dto.id());
        entity.setCreationDate(dto.creationDate());
        entity.setLastModifiedDate(dto.lastModifiedDate());
        entity.setAnalysis(dto.analysis());
        entity.setResponse(dto.response());
        entity.setCorrect(dto.isCorrect());
        if (dto.gameDto() != null) {
            entity.setGame(dto.gameDto().toEntity());
        }
        return entity;
    }

}
