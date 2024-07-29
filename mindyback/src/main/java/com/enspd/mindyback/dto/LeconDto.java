package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Lecon;

import java.time.Instant;

public record LeconDto(Integer id, Instant creationDate, Instant lastModifiedDate, String name, String description, String objectives, boolean isPassed, ChapterDto chapterDto) {

    public static LeconDto fromEntity(Lecon lecon) {
        return new LeconDto(lecon.getId(),
                lecon.getCreationDate(),
                lecon.getLastModifiedDate(),
                lecon.getName(),
                lecon.getDescription(),
                lecon.getObjectives(),
                lecon.isPassed(),
                lecon.getChapter() != null ? ChapterDto.fromEntity(lecon.getChapter()) : null
        );
    }

    public static  Lecon toEntity(LeconDto leconDto) {
        Lecon lecon = new Lecon();
        lecon.setId(leconDto.id());
        lecon.setCreationDate(leconDto.creationDate());
        lecon.setLastModifiedDate(leconDto.lastModifiedDate());
        lecon.setName(leconDto.name());
        lecon.setDescription(leconDto.description());
        lecon.setObjectives(leconDto.objectives());
        lecon.setPassed(leconDto.isPassed());
        if (leconDto.chapterDto() == null) {
            lecon.setChapter(ChapterDto.toEntity(leconDto.chapterDto()));
        }
        return lecon;
    }


}
