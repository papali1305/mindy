package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.dto.CompetenceDto;
import com.enspd.mindyback.dto.CorrectionDto;
import com.enspd.mindyback.dto.GameResponseDto;
import com.enspd.mindyback.models.Correction;
import com.enspd.mindyback.models.Game;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.enspd.mindyback.config.Utils.CORRECTION_ENDPOINT;

@Schema(description = "correction")
public interface CorrectionApi {

    @Operation(summary = " Corriger le jeu ")
    @ApiResponse(responseCode = "200", description = "Correction envoye" ,  content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompetenceDto.class)))
      @PostMapping( CORRECTION_ENDPOINT+"/corrigegame/{gameId}")
    public CorrectionDto createCorrection(@PathVariable("gameId") Integer GameId, @RequestBody GameResponseDto userResponse , @RequestHeader(name = "Authorization") String jwt);


}
