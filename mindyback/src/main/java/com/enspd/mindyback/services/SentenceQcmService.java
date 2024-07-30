package com.enspd.mindyback.services;

import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.SentenceQcm;

import java.util.List;

public interface SentenceQcmService  {
    List<SentenceQcm> createSentenceQcms(Lecon entity, String jwt);
}
