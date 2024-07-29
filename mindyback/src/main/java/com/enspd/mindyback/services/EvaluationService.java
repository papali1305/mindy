package com.enspd.mindyback.services;

import com.enspd.mindyback.models.Evaluation;
import com.enspd.mindyback.models.Lecon;

import java.util.List;

public interface EvaluationService {

    Evaluation  createEvalutation(Lecon lecon);
    
    void deleteEvaluation(Integer id);

    Evaluation updateEvaluation(Evaluation evaluation);

    Evaluation findEvaluation(Integer id);

    List<Evaluation> findAllLeconEvaluations(Integer idLecon);

    Evaluation findLastEvaluation(Integer idLecon);
}
