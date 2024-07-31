package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.Scenario;
import com.enspd.mindyback.models.ScenarioScene;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

public record ScenarioSceneDto(Integer id, Instant creationDate, Instant lastModifiedDate, String prompt , String pathToScene) {

    public  static ScenarioScene toEntity (ScenarioSceneDto dto){
        ScenarioScene scene = new ScenarioScene();
        scene.setId(dto.id);
        scene.setPrompt(dto.prompt);
        scene.setPathToScene(dto.pathToScene);
        scene.setCreationDate(dto.creationDate);
        scene.setLastModifiedDate(dto.lastModifiedDate);
        return scene;

    }
    public static ScenarioSceneDto fromEntity(ScenarioScene scene){
        return new ScenarioSceneDto(scene.getId(),
                scene.getCreationDate(),
                scene.getLastModifiedDate(),
                scene.getPrompt(),
                scene.getPathToScene()
        );
    }
}