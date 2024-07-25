package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.models.Correction;
import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.repository.CorrectionRepository;
import com.enspd.mindyback.services.CorrectionService;
import com.enspd.mindyback.services.IaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorrectionServiceImpl implements CorrectionService {

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private IaService iaService;

    @Override
    @Transactional
    public Correction createCorrection(Integer gameId, String userResponse) {
        Correction correction = iaService.corrigeGame(gameId,userResponse);
        return correctionRepository.save(correction);
    }

    @Override
    public Correction findCorrection(Integer id) {

        return correctionRepository.findById(id).orElseThrow(() -> new RuntimeException("Aucune correction trouve avec l id " + id));
    }

    @Override
    public void deleteCorrection(Integer id) {
        correctionRepository.deleteById(id);
    }
}
