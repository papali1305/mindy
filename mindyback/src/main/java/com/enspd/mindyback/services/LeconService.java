package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Lecon;

import java.util.List;

public interface LeconService {

    List<Lecon> createLecons(Integer chapterId);

    void deleteLecon(Integer leconId);

    Lecon updateLecon(Lecon lecon);

    Lecon findLecon(Integer leconId);

    List<Lecon> findLeconsByChapter(Integer chapterId);
}
