package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.dto.CompetenceDto;
import com.enspd.mindyback.models.*;

import java.util.List;

public interface IaService {

     ScenarioScene generateScene(String prompt);

     List<Scenario> createScenarios(Lecon lecon);

     List<Communication> createCommunication(Lecon lecon);

    List<Lecon> createLecons(Chapter chapter);

    Correction corrigeGame(Integer gameId, String userResponse);

    List<Chapter> createChapters(CompetenceDto competenceDto);
}
