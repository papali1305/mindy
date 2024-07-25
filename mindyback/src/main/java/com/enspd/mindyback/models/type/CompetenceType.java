package com.enspd.mindyback.models.type;


import lombok.Getter;

@Getter
public enum CompetenceType {
   NO_VERBAL_CONV("No-verbal Competence", "Skills and techniques used to communicate without words, such as facial expressions, body language, and eye contact."),
    VERBAL_CONV("Verbal Competence", "Ability to use speech and language to communicate effectively, including grammar, vocabulary, and pronunciation."),
    SOCIAL("Social Competence", "Skills to interact with others appropriately and effectively, understanding social rules and building relationships.");

    private final String name;
    private final String description;

    CompetenceType(String name, String description) {
        this.name = name;
        this.description = description;
    }

}