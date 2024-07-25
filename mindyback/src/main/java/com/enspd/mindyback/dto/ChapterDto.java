package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Chapter;

import java.time.Instant;

public record ChapterDto(Integer id, Instant creationDate, Instant lastModifiedDate, String name, String description, String objectives, boolean isCurrent , boolean isCompleted, CompetenceDto competenceDto) {


    public static ChapterDto fromEntity(Chapter chapter) {
        return new ChapterDto(chapter.getId(),
                chapter.getCreationDate(),
                chapter.getLastModifiedDate(),
                chapter.getName(),
                chapter.getDescription(),
                chapter.getObjectives(),
                chapter.isCurrent(),
                chapter.isCompleted(),
                chapter.getCompetence() != null ? CompetenceDto.fromEntity(chapter.getCompetence()) : null
                );
    }



    public static Chapter toEntity(ChapterDto dto) {
        Chapter chapter = new Chapter();
        if (dto.id() != null) {
            chapter.setId(dto.id());
        }
        chapter.setCreationDate(dto.creationDate());
        chapter.setLastModifiedDate(dto.lastModifiedDate());
        chapter.setName(dto.name());
        chapter.setDescription(dto.description());
        chapter.setObjectives(dto.objectives());
        chapter.setCurrent(dto.isCurrent());
        chapter.setCompleted(dto.isCompleted());
        if (dto.competenceDto() != null) {
            chapter.setCompetence(CompetenceDto.toEntity(dto.competenceDto()));
        }
        return chapter;
    }
}
