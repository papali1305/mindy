package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.dto.LeconDto;
import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.type.CompetenceType;

import java.util.List;

public interface LeconService {

    List<LeconDto> createLecons(Integer chapterId , String jwt);

    void deleteLecon(Integer leconId);

    LeconDto updateLecon(LeconDto leconDto);

    LeconDto findLecon(Integer leconId);

    List<LeconDto> findLeconsByChapter(Integer chapterId);

    void validateLecon(Integer id);

    CompetenceType findLeconCompetenceType(Integer leconId);
}
