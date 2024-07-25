package com.enspd.mindyback.controllers;


import com.enspd.mindyback.controllers.api.LeconApi;
import com.enspd.mindyback.dto.ChapterDto;
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
    public List<Lecon> createChapterLecons(Integer chapterId, String jwt) {

        List<Lecon> lecons = leconService.createLecons(chapterId);
        for (Lecon lecon : lecons) {
            lecon.setChapter(null);
        }

        System.out.println(lecons);
        return lecons;
    }

    @Override
    public List<Lecon> findChapterLecons(Integer ChapterId) {
        return leconService.findLeconsByChapter(ChapterId);
    }

    @Override
    public Lecon findLeconById(Integer id) {
        return leconService.findLecon(id);
    }

    @Override
    public void deleteLecon(Integer id) {
        leconService.deleteLecon(id);
    }

    @Override
    public Lecon UpdateLecon(Lecon lecon, String jwt) {
        return null;
    }
}
