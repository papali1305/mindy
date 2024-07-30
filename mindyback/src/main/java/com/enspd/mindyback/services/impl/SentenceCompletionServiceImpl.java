package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.SentenceCompletion;
import com.enspd.mindyback.repository.SentenceCompletionRepository;
import com.enspd.mindyback.services.IaService;
import com.enspd.mindyback.services.SentenceCompletionService;
import com.enspd.mindyback.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SentenceCompletionServiceImpl implements SentenceCompletionService {

    @Autowired
    private SentenceCompletionRepository sentenceCompletionRepository;

    @Autowired
    private IaService iaService;

    @Autowired
    private UserService userService;

    @Override
    public List<SentenceCompletion> createSentenceCompletions(Lecon lecon, String jwt) {
        UserDto user = userService.findUserByJwt(jwt);
        List<SentenceCompletion> sentenceCompletions = iaService.createSentenceCompletions(lecon, user);
        List<SentenceCompletion> sentenceCompletionsToSend = new ArrayList<>();
        sentenceCompletions.forEach(sentenceCompletion -> {
            sentenceCompletion.setLecon(lecon);
            sentenceCompletionsToSend.add(sentenceCompletionRepository.save(sentenceCompletion));
        });
        return sentenceCompletionRepository.saveAll(sentenceCompletionsToSend);
    }
}
