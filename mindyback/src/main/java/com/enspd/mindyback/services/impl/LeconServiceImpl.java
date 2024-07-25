package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.exception.EntityNotFoundException;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.repository.ChapterRepository;
import com.enspd.mindyback.repository.LeconRepository;
import com.enspd.mindyback.services.ChapterService;
import com.enspd.mindyback.services.IaService;
import com.enspd.mindyback.services.LeconService;
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
    
    @Override
    @Transactional

    public List<Lecon> createLecons(Integer chapterId) {
        ChapterDto chapterDto = chapterService.findChapterById(chapterId);
        List<Lecon> lecons = iaService.createLecons(ChapterDto.toEntity(chapterDto));
        List<Lecon> leconsToSend = new ArrayList<>();
        lecons.forEach((lecon) -> {
            lecon.setChapter(ChapterDto.toEntity(chapterDto));
            leconRepository.save(lecon);
            leconsToSend.add(lecon);
        }) ;
        System.out.println("envois de la liste de lecons :" +leconsToSend);
        return  leconsToSend;
    }

    @Override
    public void deleteLecon(Integer leconId) {

        leconRepository.deleteById(leconId);
    }

    @Override
    @Transactional

    public Lecon updateLecon(Lecon lecon) {
        Lecon lecon1 = findLecon(lecon.getId());
        BeanUtils.copyProperties(lecon, lecon1, "id" );
        return leconRepository.save(lecon1);
    }


    @Override
    public Lecon findLecon(Integer leconId) {
        return  leconRepository.findById(leconId).orElseThrow(()-> new EntityNotFoundException("lecon non trouve avec l'id " +leconId, ErrorCodes.LECON_NOT_FOUND));
    }

    @Override
    public List<Lecon> findLeconsByChapter(Integer chapterId) {
        return  leconRepository.findByChapterId(chapterId).orElseThrow(() -> new EntityNotFoundException("aucune lecon  trouve pour le chapitre avec l'id " +chapterId , ErrorCodes.LECONS_NOT_FOUND));
    }
}
