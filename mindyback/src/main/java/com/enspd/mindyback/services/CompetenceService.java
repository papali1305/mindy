package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.CompetenceDto;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.Scenario;

import java.util.List;

public interface CompetenceService {

    CompetenceDto createCompetence(CompetenceDto competenceDto);

    CompetenceDto updateCompetence(CompetenceDto competenceDto);

    CompetenceDto getCompetence(Integer id);


    List<CompetenceDto> getAllCompetences(Integer idUser);
}
