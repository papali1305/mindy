package com.enspd.mindyback.services;

import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.models.ScenarioScene;

import java.util.List;

public interface IaService {

     ScenarioScene generateScene(String prompt);

     List<Scenario> createScenarios(Lecon lecon);

     List<Communication> createCommunication(Lecon lecon);
}
