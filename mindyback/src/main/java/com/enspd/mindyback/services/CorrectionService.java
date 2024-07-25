package com.enspd.mindyback.services;

import com.enspd.mindyback.models.Correction;
import com.enspd.mindyback.models.Game;

public interface CorrectionService {

    Correction  createCorrection(Integer gameId, String userResponse);

    Correction findCorrection(Integer id);


    void   deleteCorrection(Integer id);
}
