package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.models.ScenarioScene;
import com.enspd.mindyback.services.IaService;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;

public class IaServiceImpl implements IaService {

    private String huggingFaceToken = "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";
    private String huggingFacStabilityurl = "https://api-inference.huggingface.co/models/stabilityai/stable-diffusion-3-medium-diffusers";

    public static void main(String[] args) {
        IaServiceImpl iaService = new IaServiceImpl();
        ScenarioScene scenarioScene = iaService.generateScene("Une personne semble perdue dans l'ensemble d'un centre commercial, que feriez-vous ?");
        System.out.println(scenarioScene.getPathToScene());
    }
    @Override
    public ScenarioScene generateScene(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + huggingFaceToken);

        JSONObject body = new JSONObject();
        body.put("inputs", prompt);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.exchange(huggingFacStabilityurl, HttpMethod.POST, entity, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {

                ImageServiceImpl imageService = new ImageServiceImpl();
                String imagePath = imageService.saveImage(response.getBody());
                ScenarioScene scenarioScene = new ScenarioScene();
                scenarioScene.setPathToScene(imagePath);
                scenarioScene.setPrompt(prompt);
                return scenarioScene;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("l'ia n a pas pu generer la scène");
        }
    }
}
