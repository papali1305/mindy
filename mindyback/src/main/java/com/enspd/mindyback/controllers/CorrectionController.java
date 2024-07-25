package com.enspd.mindyback.controllers;

import com.enspd.mindyback.controllers.api.CorrectionApi;
import com.enspd.mindyback.models.Correction;
import com.enspd.mindyback.models.Game;
import com.enspd.mindyback.services.CorrectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static com.enspd.mindyback.config.Utils.CORRECTION_ENDPOINT;

@RestController(CORRECTION_ENDPOINT)
public class CorrectionController implements CorrectionApi {

    @Autowired
    private CorrectionService correctionService;


    @Override
    public Correction createCorrection(Integer GameId, String userResponse , String jwt) {

        Correction correction = correctionService.createCorrection(GameId, userResponse);
        correction.setGame(null);
        return correction;
    }
}
