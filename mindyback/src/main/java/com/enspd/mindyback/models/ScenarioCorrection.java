package com.enspd.mindyback.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ScenarioCorrection extends AbstractEntity {
    private String analysis;
    private String response;
}
