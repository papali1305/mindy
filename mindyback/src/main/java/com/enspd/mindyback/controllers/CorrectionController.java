package com.enspd.mindyback.controllers;

import com.enspd.mindyback.controllers.api.CorrectionApi;
import com.enspd.mindyback.dto.CorrectionDto;
import com.enspd.mindyback.dto.GameResponseDto;
import com.enspd.mindyback.services.CorrectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static com.enspd.mindyback.config.Utils.CORRECTION_ENDPOINT;

@RestController(CORRECTION_ENDPOINT)
public class CorrectionController implements CorrectionApi {

    @Autowired
    private CorrectionService correctionService;


    @Override
    public CorrectionDto createCorrection(Integer GameId, GameResponseDto userResponse, String jwt) {

        CorrectionDto correction = correctionService.createCorrection(GameId, userResponse, jwt);
        return correction;
    }
}
