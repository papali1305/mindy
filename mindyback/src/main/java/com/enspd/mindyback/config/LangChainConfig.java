package com.enspd.mindyback.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainConfig {

    @Bean
    public ChatLanguageModel geminiChatModel() {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
                .project("mindy-432210")
                .location("northamerica-northeast1")
                .modelName("gemini-1.5-pro")
                .maxRetries(3)
                .temperature(1f)
                .topP(1F)
                .build();


        return model;
    }
}
