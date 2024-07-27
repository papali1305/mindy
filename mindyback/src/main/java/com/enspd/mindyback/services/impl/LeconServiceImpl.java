package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.dto.LeconDto;
import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.exception.EntityNotFoundException;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.exception.InvalidOperationException;
import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.type.CompetenceType;
import com.enspd.mindyback.repository.ChapterRepository;
import com.enspd.mindyback.repository.LeconRepository;
import com.enspd.mindyback.services.ChapterService;
import com.enspd.mindyback.services.IaService;
import com.enspd.mindyback.services.LeconService;
import com.enspd.mindyback.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeconServiceImpl implements LeconService {

    @Autowired
    private LeconRepository leconRepository;


    @Autowired
    private ChapterService chapterService;

    @Autowired
    private IaService iaService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional

    public List<LeconDto> createLecons(Integer chapterId, String jwt) {
        ChapterDto chapterDto = chapterService.findChapterById(chapterId);
        UserDto userDto = userService.findUserByJwt(jwt);
        if (chapterDto.isCurrent() || chapterDto.isCompleted()) {
            try {
                List<Lecon> lecons = iaService.createLecons(ChapterDto.toEntity(chapterDto), userDto);

                lecons.forEach((lecon) -> {
                    lecon.setChapter(ChapterDto.toEntity(chapterDto));
                });
                return leconRepository.saveAll(lecons).stream().map(LeconDto::fromEntity).toList();

            } catch (EntityNotFoundException e) {
                throw new InvalidOperationException("Erreur lors de la création des lecons", ErrorCodes.LECONS_CREATION_ERROR);
            }

        } else {
            throw new InvalidOperationException("Les chapitres precedents n ont pas encore ete complete", ErrorCodes.CHAPTER_NOT_COMPLETE);
        }
    }

    @Override
    public void deleteLecon(Integer leconId) {

        leconRepository.deleteById(leconId);
    }

    @Override
    @Transactional

    public LeconDto updateLecon(LeconDto lecon) {
        LeconDto lecon1 = findLecon(lecon.id());
        BeanUtils.copyProperties(lecon, lecon1, "id");
        return LeconDto.fromEntity(leconRepository.save(LeconDto.toEntity(lecon1)));
    }


    @Override
    public LeconDto findLecon(Integer leconId) {
        return LeconDto.fromEntity(leconRepository.findById(leconId).orElseThrow(() -> new EntityNotFoundException("lecon non trouve avec l'id " + leconId, ErrorCodes.LECON_NOT_FOUND)));
    }

    @Override
    public List<LeconDto> findLeconsByChapter(Integer chapterId) {
        return leconRepository.findByChapterId(chapterId).orElseThrow(() -> new EntityNotFoundException("aucune lecon  trouve pour le chapitre avec l'id " + chapterId, ErrorCodes.LECONS_NOT_FOUND)).stream().map(LeconDto::fromEntity).toList();
    }

    @Override
    public void validateLecon(Integer id) {
        Lecon lecon = leconRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Impossible de valider le lecon avec l id :" + id, ErrorCodes.LECON_NOT_FOUND));
        lecon.setPassed(true);
        leconRepository.save(lecon);
    }

    @Override
    public CompetenceType findLeconCompetenceType(Integer leconId) {
        return CompetenceType.valueOf(
                leconRepository.findCompetenceTypeByLeconId(leconId).orElseThrow(() -> new EntityNotFoundException("Impossible de trouver le type de competence pour le lecon avec l id :" + leconId, ErrorCodes.COMPETENCE_TYPE_NOT_FOUND)));
    }
}
