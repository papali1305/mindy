package com.enspd.mindyback.controllers;

import com.enspd.mindyback.controllers.api.CompetenceApi;
import com.enspd.mindyback.dto.CompetenceDto;
import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.models.User;
import com.enspd.mindyback.services.CompetenceService;
import com.enspd.mindyback.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.enspd.mindyback.config.Utils.COMPETENCE_ENDPOINT;

@RestController(COMPETENCE_ENDPOINT)
public class CompetenceController implements CompetenceApi {

    @Autowired
    private CompetenceService competenceService;

    @Autowired
    private UserService userService;
    @Override
    public CompetenceDto createCompetence(CompetenceDto competenceDto, String jwt) {
        return competenceService.createCompetence(competenceDto);
    }

    @Override
    public CompetenceDto updateCompetence(CompetenceDto competenceDto, String jwt) {
        return competenceService.updateCompetence(competenceDto);
    }

    @Override
    public CompetenceDto getCompetence(Integer id , String jwt) {
        return competenceService.getCompetence(id);
    }

    @Override
    public List<CompetenceDto> getAllCompetences( String jwt) {
        UserDto user = userService.findUserByJwt(jwt);
        return competenceService.getAllCompetences(user.id());
    }
}
