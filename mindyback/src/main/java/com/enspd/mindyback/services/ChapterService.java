package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.ChapterDto;

import java.util.List;

public interface ChapterService {

    public ChapterDto createChapter(ChapterDto chapterDto);

    public ChapterDto findChapterById(Integer id);

    public ChapterDto updateChapter(ChapterDto chapterDto);

    public void deleteChapter(Integer id);

    public void updateChapterCurrent(Integer id);

    public void validateChapter(Integer id);

    public ChapterDto getChapter(Integer id);

    public List<ChapterDto> getAllChapters();

    List<ChapterDto> createAllChapters(List<ChapterDto> chaptersDto);

    List<ChapterDto> getAllChapterByCompetence(Integer idCompetence);
}
