package com.enspd.mindyback.controllers;

import com.enspd.mindyback.controllers.api.ChapterApi;
import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.services.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.enspd.mindyback.config.Utils.CHAPTER_ENDPOINT;


@RestController(CHAPTER_ENDPOINT)
public class ChapterController implements ChapterApi {

    @Autowired
    private ChapterService chapterService;

    @Override
    public ChapterDto getChapterById(Integer id, String jwt) {
        return chapterService.findChapterById(id);
    }

    @Override
    public List<ChapterDto> getAllChapter(String jwt) {
        return chapterService.getAllChapters();
    }

    @Override
    public List<ChapterDto> getAllChapterByCompetence(Integer idCompetence, String jwt) {

        return chapterService.getAllChapterByCompetence(idCompetence);
    }

    @Override
    public void updateChapterCurrent(Integer id) {
        chapterService.updateChapterCurrent(id);
    }

    @Override
    public void validateChapter(Integer id) {
        chapterService.validateChapter(id);
    }
}

