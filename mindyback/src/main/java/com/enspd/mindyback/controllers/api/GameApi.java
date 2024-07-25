package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.models.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.enspd.mindyback.config.Utils.GAME_ENDPOINT;
import static com.enspd.mindyback.config.Utils.LECON_ENDPOINT;

public interface GameApi {


    @GetMapping(GAME_ENDPOINT + "/scenario/createbylecon/{leconId}")
    public List<Scenario> createLeconScenarioGames(@PathVariable("leconId") Integer leconId, @RequestHeader(name = "Authorization") String jwt);

    @PostMapping(GAME_ENDPOINT + "/communication/createbylecon/{leconId}")
    public List<Communication> createLeconCommunicationGames(@PathVariable("leconId") Integer leconId, @RequestHeader(name = "Authorization") String jwt);



    @GetMapping(GAME_ENDPOINT +"/scenario/findById/{id}")
    public Scenario findGameScenarioById(Integer id);

    @GetMapping(GAME_ENDPOINT +"/communication/findById/{id}")
    public Communication findGameCommunicationById(Integer id);


}
