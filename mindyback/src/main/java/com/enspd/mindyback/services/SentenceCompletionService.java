package com.enspd.mindyback.services;

import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.SentenceCompletion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface SentenceCompletionService {
    List<SentenceCompletion> createSentenceCompletions(Lecon entity, String jwt);
}
