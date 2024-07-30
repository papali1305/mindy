package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Correction;
import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.SentenceCompletion;
import com.enspd.mindyback.models.type.GameType;
import jakarta.persistence.*;

import java.time.Instant;

public record SentenceCompletionDto(Integer id,

                                    String name,
                                    String description,
                                    GameType type,
                                    String userResponse,
                                    CorrectionDto correction,
                                    Instant creationDate,
                                    Instant lastModifiedDate,
                                    String sentenceToComplete,
                                    String context,
                                    boolean isPassed) implements GameDto {

    public static SentenceCompletionDto fromEntity(SentenceCompletion entity) {
        return new SentenceCompletionDto(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getType(),
                entity.getUserResponse(),
                entity.getCorrection() == null ? null : CorrectionDto.fromEntity(entity.getCorrection()),
                entity.getCreationDate(),
                entity.getLastModifiedDate(),
                entity.getSentenceToComplete(),
                entity.getContext(),
                entity.isPassed());
    }


    @Override
    public Game toEntity() {
        SentenceCompletion game = new SentenceCompletion();
        game.setId(this.id);
        game.setCreationDate(this.creationDate);
        game.setLastModifiedDate(this.lastModifiedDate);
        game.setName(this.name);
        game.setDescription(this.description);
        game.setPassed(this.isPassed);
        game.setUserResponse(this.userResponse);
        game.setType(this.type);
        game.setSentenceToComplete(this.sentenceToComplete);
        game.setContext(this.context);
        if (this.correction != null) {
            game.setCorrection(CorrectionDto.toEntity(this.correction));
        }
        return game;
    }
}
