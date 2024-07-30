package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.dto.CompetenceDto;
import com.enspd.mindyback.dto.GameDto;
import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Scenario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static com.enspd.mindyback.config.Utils.GAME_ENDPOINT;

public interface GameApi {

    @PostMapping(GAME_ENDPOINT + "/createByLeconId/{leconId}")
    @Operation(summary = "Creer les jeux d une lecon ")
    @ApiResponse(responseCode = "200", description = "Jeux cree", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompetenceDto.class)))
    public List<GameDto> createLeconGames(@PathVariable("leconId") Integer leconId, @RequestHeader(name = "Authorization") String jwt);


    @GetMapping(GAME_ENDPOINT + "/scenario/createbylecon/{leconId}")
    public List<Scenario> createLeconScenarioGames(@PathVariable("leconId") Integer leconId, @RequestHeader(name = "Authorization") String jwt);

    @PostMapping(GAME_ENDPOINT + "/communication/createbylecon/{leconId}")
    public List<Communication> createLeconCommunicationGames(@PathVariable("leconId") Integer leconId, @RequestHeader(name = "Authorization") String jwt);



    @GetMapping(GAME_ENDPOINT +"/scenario/findById/{id}")
    public Scenario findGameScenarioById(Integer id);

    @GetMapping(GAME_ENDPOINT +"/communication/findById/{id}")
    public Communication findGameCommunicationById(Integer id);


}
