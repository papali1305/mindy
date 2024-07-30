package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.SentenceQcm;
import com.enspd.mindyback.repository.SentenceQcmRepository;
import com.enspd.mindyback.services.IaService;
import com.enspd.mindyback.services.SentenceQcmService;
import com.enspd.mindyback.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SentenceQcmServiceImpl implements SentenceQcmService {

    @Autowired
    private SentenceQcmRepository sentenceQcmRepository;
    @Autowired
    private IaService iaService;
    @Autowired
    private UserService userService;

    @Override
    public List<SentenceQcm> createSentenceQcms(Lecon entity, String jwt) {
        UserDto user = userService.findUserByJwt(jwt);
        List<SentenceQcm> sentenceQcms = iaService.createSentenceQcms(entity, user);
        List<SentenceQcm> sentenceQcmsToSend = new java.util.ArrayList<>();
        sentenceQcms.forEach(sentenceQcm -> {
            sentenceQcm.setLecon(entity);
            sentenceQcmsToSend.add(sentenceQcmRepository.save(sentenceQcm));
        });
        return sentenceQcmRepository.saveAll(sentenceQcmsToSend);
    }
}
