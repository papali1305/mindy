package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.exception.EntityNotFoundException;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.models.Evaluation;
import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.repository.EvaluationRepository;
import com.enspd.mindyback.services.EvaluationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public Evaluation createEvalutation(Lecon lecon) {
        int note = 0;

        List<Evaluation> pastEvaluations = findAllLeconEvaluations(lecon.getId());
        Evaluation evaluation = new Evaluation();

        if (pastEvaluations != null) {
            Evaluation last = pastEvaluations.get( pastEvaluations.size() - 1 );
            evaluation.setNote(note);
            evaluation.setProgress(note - last.getProgress());
            evaluation.setEvaluationNumber(last.getEvaluationNumber() + 1);
            evaluation.setComment("");
        } else {
            evaluation.setNote(note);
            evaluation.setProgress(note);
            evaluation.setEvaluationNumber(1);
            evaluation.setComment("");
        }
        evaluation.setLecon(lecon);
        return evaluation;

    }

    @Override
    public void deleteEvaluation(Integer id) {

        evaluationRepository.deleteById(id);
    }

    @Override
    public Evaluation updateEvaluation(Evaluation evaluation) {


        Evaluation existingEvaluation = findEvaluation(evaluation.getId());
        BeanUtils.copyProperties(evaluation, existingEvaluation, "id");
        return evaluationRepository.save(existingEvaluation);
    }

    @Override
    public Evaluation findEvaluation(Integer id) {
        return evaluationRepository.findById(id).orElseThrow(() -> new RuntimeException("Aucune evalutation trouve avec l id :" + id));
    }

    @Override
    public List<Evaluation> findAllLeconEvaluations(Integer idLecon) {
        try {
            Optional<List<Evaluation>> optionalEvaluation = evaluationRepository.findAllByLeconIdOrderByCreationDate(idLecon);
            return optionalEvaluation.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Evaluation findLastEvaluation(Integer idLecon) {
        return evaluationRepository.findFirstByOOrderByCreationDateDescBy(idLecon).orElseThrow(() -> new EntityNotFoundException("Aucune evaluation trouve avec l id :" + idLecon, ErrorCodes.EVALUATION_NOT_FOUND));
    }
}
