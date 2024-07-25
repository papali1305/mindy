package com.enspd.mindyback.controllers;


import com.enspd.mindyback.controllers.api.LeconApi;
import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.dto.LeconDto;
import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.services.ChapterService;
import com.enspd.mindyback.services.LeconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.enspd.mindyback.config.Utils.LECON_ENDPOINT;

@RestController(LECON_ENDPOINT)
public class LeconController implements LeconApi {

    @Autowired
    private LeconService leconService;
    @Autowired
    private ChapterService chapterService;

    @Override
    public List<LeconDto> createChapterLecons(Integer chapterId, String jwt) {

        List<LeconDto> lecons = leconService.createLecons(chapterId, jwt);

        return lecons;
    }

    @Override
    public List<LeconDto> findChapterLecons(Integer ChapterId) {
        return leconService.findLeconsByChapter(ChapterId);
    }

    @Override
    public LeconDto findLeconById(Integer id) {
        return leconService.findLecon(id);
    }

    @Override
    public void deleteLecon(Integer id) {
        leconService.deleteLecon(id);
    }

    @Override
    public Lecon UpdateLecon(LeconDto leconDto, String jwt) {
        return null;
    }
}
