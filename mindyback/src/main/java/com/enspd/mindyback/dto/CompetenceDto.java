package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Competence;
import com.enspd.mindyback.models.type.CompetenceType;

import java.time.Instant;


public record CompetenceDto(Integer id, Instant creationDate, Instant lastModifiedDate, String name, String description,
                            CompetenceType competenceType, float score, UserDto user) {

    public static CompetenceDto fromEntity(Competence competence) {
        return new CompetenceDto(competence.getId(),
                competence.getCreationDate(),
                competence.getLastModifiedDate(),
                competence.getName(),
                competence.getDescription(), competence.getCompetenceType(),
                competence.getScore(),
                competence.getUser() != null ? UserDto.fromEntity(competence.getUser()) : null);
    }

    public static Competence toEntity(CompetenceDto dto) {
        Competence competence = new Competence();
        competence.setId(dto.id());
        competence.setCreationDate(dto.creationDate());
        competence.setLastModifiedDate(dto.lastModifiedDate());
        competence.setName(dto.name());
        competence.setDescription(dto.description());
        competence.setCompetenceType(dto.competenceType());
        competence.setScore(dto.score());
        if (dto.user() != null) {
            competence.setUser(UserDto.toEntity(dto.user()));
        }


        return competence;
    }


}
