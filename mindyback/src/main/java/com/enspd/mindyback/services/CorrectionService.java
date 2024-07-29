package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.CorrectionDto;
import com.enspd.mindyback.dto.GameResponseDto;
import com.enspd.mindyback.models.Correction;

public interface CorrectionService {

    CorrectionDto createCorrection(Integer gameId, GameResponseDto userResponse, String jwt);

    Correction findCorrection(Integer id);


    void   deleteCorrection(Integer id);
}
