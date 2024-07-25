package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.dto.CompetenceDto;
import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.exception.EntityNotFoundException;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Competence;
import com.enspd.mindyback.models.User;
import com.enspd.mindyback.repository.CompetenceRepository;
import com.enspd.mindyback.services.ChapterService;
import com.enspd.mindyback.services.CompetenceService;
import com.enspd.mindyback.services.IaService;
import com.enspd.mindyback.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;

@Service
public class CompetenceServiceImpl implements CompetenceService {

    @Autowired
    private CompetenceRepository competenceRepository;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private IaService iaService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public CompetenceDto createCompetence(CompetenceDto competenceDto ) {
        UserDto userDto = competenceDto.user();
        List<Chapter> chapters = iaService.createChapters(competenceDto, userDto);
        List<ChapterDto> chaptersDto = new ArrayList<>();
        Competence competenceSaved = competenceRepository.save(CompetenceDto.toEntity(competenceDto));
        for (Chapter chapter : chapters) {
            chapter.setCompetence(competenceSaved);
            chaptersDto.add(ChapterDto.fromEntity(chapter));
        }
        chapterService.createAllChapters(chaptersDto);

        return CompetenceDto.fromEntity(competenceSaved);
    }

    @Override
    public CompetenceDto updateCompetence(CompetenceDto competenceDto) {
        return CompetenceDto.fromEntity(competenceRepository.save(CompetenceDto.toEntity(competenceDto)));
    }

    @Override
    public CompetenceDto getCompetence(Integer id) {
        return competenceRepository.findById(id).map(CompetenceDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException("Competence not found with id :" + id, ErrorCodes.COMPETENCE_NOT_FOUND));
    }

    @Override
    public List<CompetenceDto> getAllCompetences(Integer idUser) {
        return competenceRepository.findAllByUserId(idUser).orElseThrow(() ->
                new EntityNotFoundException("Aucune competence trouvée pour l'utilisateur " + idUser, ErrorCodes.COMPETENCE_NOT_FOUND)
        ).stream().map(CompetenceDto::fromEntity).toList();
    }
}
