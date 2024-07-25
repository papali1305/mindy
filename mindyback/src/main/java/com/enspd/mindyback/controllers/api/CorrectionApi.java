package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.models.Correction;
import com.enspd.mindyback.models.Game;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.enspd.mindyback.config.Utils.CORRECTION_ENDPOINT;

public interface CorrectionApi {

    @PostMapping( CORRECTION_ENDPOINT+"/corrigegame/{gameId}")
    public Correction createCorrection(@PathVariable("gameId") Integer GameId,@RequestBody String userResponse ,@RequestHeader(name = "Authorization") String jwt);


}
