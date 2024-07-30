package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.CompetenceDto;

import java.util.List;

public interface CompetenceService {

    CompetenceDto createCompetence(CompetenceDto competenceDto);

    CompetenceDto updateCompetence(CompetenceDto competenceDto);

    CompetenceDto findCompetenceById(Integer id);


    List<CompetenceDto> findAllCompetences(Integer idUser);
}
