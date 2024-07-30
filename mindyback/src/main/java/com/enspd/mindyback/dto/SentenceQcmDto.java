package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.models.SentenceQcm;

import java.time.Instant;

public record SentenceQcmDto(Integer id,
                             Instant creationDate,
                             Instant lastModifiedDate,
                             String name,
                             String description,
                             String userResponse,
                             boolean isPassed,
                             String sentenceToComplete,
                             String words,
                             String response,
                             CorrectionDto correction) implements GameDto {
    @Override
    public Game toEntity() {
        SentenceQcm game = new SentenceQcm();
        game.setId(this.id);
        game.setCreationDate(this.creationDate);
        game.setLastModifiedDate(this.lastModifiedDate);
        game.setName(this.name);
        game.setDescription(this.description);
        game.setPassed(this.isPassed);
        game.setUserResponse(this.userResponse);
        game.setSentenceToComplete(this.sentenceToComplete);
        game.setWords(this.words);
        game.setResponse(this.response);
        if (this.correction != null) {
            game.setCorrection(CorrectionDto.toEntity(this.correction));
        }
        return game;
    }
    public static SentenceQcmDto fromEntity(SentenceQcm game) {
        return new SentenceQcmDto(game.getId(),
                game.getCreationDate(),
                game.getLastModifiedDate(),
                game.getName(),
                game.getDescription(),
                game.getUserResponse(),
                game.isPassed(),
                game.getSentenceToComplete(),
                game.getWords(),
                game.getResponse(),
                game.getCorrection() != null ? CorrectionDto.fromEntity(game.getCorrection()) : null);
    }
}
