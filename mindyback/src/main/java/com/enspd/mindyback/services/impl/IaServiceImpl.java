package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.models.*;
import com.enspd.mindyback.services.IaService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.ai.mistralai.MistralAiChatOptions;
import org.springframework.ai.mistralai.api.MistralAiApi;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class IaServiceImpl implements IaService {

    private String huggingFaceToken = "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";

    private String mistralAiToken = "AbAYsdqyrpXARADOaeFR2MrYasYWuZoq";

    private String huggingFacStabilityurl = "https://api-inference.huggingface.co/models/stabilityai/stable-diffusion-3-medium-diffusers";
    private String huggingFaceLLAMA3url = "https://api-inference.huggingface.co/models/meta-llama/Meta-Llama-3-8B-Instruct/v1/chat/completions";

    public static void main(String[] args) {
        IaServiceImpl iaService = new IaServiceImpl();
      /*  ScenarioScene scenarioScene = iaService.generateScene("Une personne semble perdue dans l'ensemble d'un centre commercial, que feriez-vous ?");
        System.out.println(scenarioScene.getPathToScene());*/
        Lecon lecon = new Lecon();
        lecon.setNom("prendre ses distances de quelqu'un");
        lecon.setDescription("Cette leçon vise à enseigner à l'enfant à prendre ses distances de quelqu'un. La leçon utilisera des jeux de rôle et des activités interactives pour pratiquer et maîtriser ces gestes.");
        lecon.setObjSpec("Apprendre à prendre ses distances de quelqu'un dans des contextes sociaux variés.");
        System.out.println(iaService.createScenarios(lecon));

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

    /*public Scenario createScenario(Lecon lecon) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + huggingFaceToken);


        JSONObject body = new JSONObject();

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", """
                
                        Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation spécialisée, travail social, psychomotricité et intégration sensorielle ; le tout dans le domaine de l'autisme. Tu as établi un programme pour un autiste et un ensemble de leçons.\s
                                        
                        Le système a été pensé ainsi : à partir des informations sur la leçon du jour, tu crées des scénarios qui sont des jeux. D'abord, tu décides du scénario et de ce qu'il aborde au sujet de la leçon. Après l'avoir décidé, tu génères une description de la scène qui sera plus tard représentée en une image. Ensuite, tu génères une question sur cette scène. L'autiste répond et tu apprécies sa réponse.
                                        
                        Voici où nous en sommes :
                        Nous sommes sur la leçon """ + lecon.getNom() + """
                        parlant de """ + lecon.getDescription() + """
                        avec pour objectif """ + lecon.getObjSpec() + """
                                        
                        Le scénario est composé d'une image qui sera envoyée avec un contexte. L'utilisateur est un autiste pour lui apprendre cette leçon.
                        Tu vas décrire 5 scénarios portant sur la leçon. Chaque scénario est composé des champs suivants :
                        - scenarioName : "le nom du scénario";
                        - scenarioDescription : "la description du scénario.";
                        - aiQuestion : "la question à poser à l'autiste portant sur la leçon et le scénario que tu as généré";
                        - prompt : "un prompt qui sera envoyé à une IA de génération d'image qui va, à partir de ce prompt, créer une image correspondante à la description du scénario puis sera envoyée à l'utilisateur";
                        - scenarioType : "peut être soit RECOGNIZE_FACIAL_EXPRESSIONS, APPROPRIATE_REACTIONS, UNDERSTAND_EMOTIONS, COMMUNICATION_SKILLS, CONFLICT_RESOLUTION, GROUP_ACTIVITIES, DAILY_LIFE_SKILLS, SCHOOL_WORK_BEHAVIOR".
                                        
                        Attention :
                        Le résultat doit être au format JSON avec uniquement les informations demandées. Ne répond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées. N'oublie pas de remplir tous les champs.
                        
                        Exemple de format JSON attendu :
                        [
                          {
                            "scenarioName": "Nom du scénario",
                            "scenarioDescription": "Description du scénario.",
                            "aiQuestion": "Question à poser à l'autiste.",
                            "prompt": "Prompt pour l'IA de génération d'image.",
                            "scenarioType": "Type de scénario"
                          },
                          ...
                        ]
                                        
                """
              ));
        //   messages.put(new JSONObject().put("role", "user").put("content", "message user"));


        body.put("messages", messages);
        body.put("model", "meta-llama/Meta-Llama-3-70B-Instruct");


        //  body.put("max_tokens", 500);
        body.put("stream", false);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(huggingFaceLLAMA3url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JSONObject jsonObject = new JSONObject(response.getBody());
                System.out.println(jsonObject);
                JSONArray choicesArray = jsonObject.getJSONArray("choices");
                JSONObject firstChoice = choicesArray.getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                JSONObject contentJson = new JSONObject(message.getString("content"));
                ScenarioScene scenarioScene = generateScene(contentJson.getString("prompt"));
                Scenario scenario = new Scenario();
                scenario.setScenarioName(contentJson.getString("scenarioName"));
                scenario.setAiQuestion(contentJson.getString("aiQuestion"));
                scenario.setScenarioDescription(contentJson.getString("scenarioDescription"));
                String type = contentJson.getString("scenarioType");
                switch (type) {
                    case "RECOGNIZE_FACIAL_EXPRESSIONS":
                        scenario.setScenarioType(ScenarioType.RECOGNIZE_FACIAL_EXPRESSIONS);
                        break;

                    case "APPROPRIATE_REACTIONS":

                        scenario.setScenarioType(ScenarioType.APPROPRIATE_REACTIONS);
                        break;

                    case "UNDERSTAND_EMOTIONS":

                        scenario.setScenarioType(ScenarioType.UNDERSTAND_EMOTIONS);
                        break;

                    case "COMMUNICATION_SKILLS":

                        scenario.setScenarioType(ScenarioType.COMMUNICATION_SKILLS);
                        break;

                    case "CONFLICT_RESOLUTION":

                        scenario.setScenarioType(ScenarioType.CONFLICT_RESOLUTION);
                        break;

                    case "GROUP_ACTIVITIES":

                        scenario.setScenarioType(ScenarioType.GROUP_ACTIVITIES);
                        break;
                    case "DAILY_LIFE_SKILLS":

                        scenario.setScenarioType(ScenarioType.DAILY_LIFE_SKILLS);
                        break;

                    case "SCHOOL_WORK_BEHAVIOR":

                        scenario.setScenarioType(ScenarioType.SCHOOL_WORK_BEHAVIOR);
                        break;


                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("l'ia n a pas pu generer la scène");
        }
        return null;
    }*/

    @Override

    public List<Scenario> createScenarios(Lecon lecon) {

        var mistralAiApi = new MistralAiApi(mistralAiToken);

        var chatModel = new MistralAiChatModel(mistralAiApi, MistralAiChatOptions.builder()
                .withModel(MistralAiApi.ChatModel.OPEN_MIXTRAL_22B.getValue())
                .withTemperature(1f)
                .withTopP(1F)
                .build());

        ChatResponse response = chatModel.call(
                new Prompt("""
                                                   
                                   Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation spécialisée, travail social, psychomotricité et intégration sensorielle ; le tout dans le domaine de l'autisme. Tu as établi un programme pour un autiste et un ensemble de leçons.\s
                                                   
                                   Le système a été pensé ainsi : à partir des informations sur la leçon du jour, tu crées des scénarios qui sont des jeux. D'abord, tu décides du scénario et de ce qu'il aborde au sujet de la leçon. Après l'avoir décidé, tu génères une description de la scène qui sera plus tard représentée en une image. Ensuite, tu génères une question sur cette scène. L'autiste répond et tu apprécies sa réponse.
                                                   
                                   Voici où nous en sommes :
                                   Nous sommes sur la leçon """ + lecon.getNom() + """
                                   parlant de """ + lecon.getDescription() + """
                                   avec pour objectif """ + lecon.getObjSpec() + """
                                                           
                                           Le scénario est composé d'une image qui sera envoyée avec un contexte. L'utilisateur est un autiste pour lui apprendre cette leçon.
                                           Tu vas décrire 5 scénarios portant sur la leçon. Chaque scénario est composé des champs suivants :
                                           - scenarioName : "le nom du scénario";
                                           - scenarioDescription : "la description du scénario.";
                                           - aiQuestion : "la question à poser à l'autiste portant sur la leçon et le scénario que tu as généré";
                                           - prompt : "un prompt qui sera envoyé à une IA de génération d'image qui va, à partir de ce prompt, créer une image correspondante à la description du scénario puis sera envoyée à l'utilisateur. 
                                           Lors de l ecriture du prompt prends en compte que la pluspart des personnes doivent etre noir que l'ia doit mettre l'accent sur certains details de la scene que tu preciseras";
                                           - scenarioType : "peut être soit RECOGNIZE_FACIAL_EXPRESSIONS, APPROPRIATE_REACTIONS, UNDERSTAND_EMOTIONS, COMMUNICATION_SKILLS, CONFLICT_RESOLUTION, GROUP_ACTIVITIES, DAILY_LIFE_SKILLS, SCHOOL_WORK_BEHAVIOR".
                                                           
                                           Attention :
                                           Le résultat doit être au format JSON avec uniquement les informations demandées. Ne répond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées. N'oublie pas de remplir tous les champs.
                                           
                                           Exemple de format JSON attendu :
                                           [
                                             {
                                               "scenarioName": "Nom du scénario",
                                               "scenarioDescription": "Description du scénario.",
                                               "aiQuestion": "Question à poser à l'autiste.",
                                               "prompt": "Prompt pour l'IA de génération d'image.",
                                               "scenarioType": "Type de scénario"
                                             },
                                             ...
                                           ]
                                                           
                                   """
                ));

        try {

            String resp = response.getResult().getOutput().getContent();
            JSONArray jsonArray = new JSONArray(resp);
            List<Scenario> scenarios = new ArrayList<>();
            jsonArray.forEach(jsonElement -> {
                JSONObject contentJson = (JSONObject) jsonElement;
                ScenarioScene scenarioScene = generateScene(contentJson.getString("prompt"));
                Scenario scenario = new Scenario();
                scenario.setName(contentJson.getString("scenarioName"));
                scenario.setAiQuestion(contentJson.getString("aiQuestion"));
                scenario.setDescription(contentJson.getString("scenarioDescription"));
                scenario.setScenarioScene(scenarioScene);
                String type = contentJson.getString("scenarioType");
                switch (type) {
                    case "RECOGNIZE_FACIAL_EXPRESSIONS":
                        scenario.setScenarioType(ScenarioType.RECOGNIZE_FACIAL_EXPRESSIONS);
                        break;

                    case "APPROPRIATE_REACTIONS":

                        scenario.setScenarioType(ScenarioType.APPROPRIATE_REACTIONS);
                        break;

                    case "UNDERSTAND_EMOTIONS":

                        scenario.setScenarioType(ScenarioType.UNDERSTAND_EMOTIONS);
                        break;

                    case "COMMUNICATION_SKILLS":

                        scenario.setScenarioType(ScenarioType.COMMUNICATION_SKILLS);
                        break;

                    case "CONFLICT_RESOLUTION":

                        scenario.setScenarioType(ScenarioType.CONFLICT_RESOLUTION);
                        break;

                    case "GROUP_ACTIVITIES":

                        scenario.setScenarioType(ScenarioType.GROUP_ACTIVITIES);
                        break;
                    case "DAILY_LIFE_SKILLS":

                        scenario.setScenarioType(ScenarioType.DAILY_LIFE_SKILLS);
                        break;

                    case "SCHOOL_WORK_BEHAVIOR":

                        scenario.setScenarioType(ScenarioType.SCHOOL_WORK_BEHAVIOR);
                        break;


                }
                scenarios.add(scenario);

            });
            return scenarios;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Communication> createCommunication(Lecon lecon) {

        var mistralAiApi = new MistralAiApi(mistralAiToken);

        var chatModel = new MistralAiChatModel(mistralAiApi, MistralAiChatOptions.builder()
                .withModel(MistralAiApi.ChatModel.OPEN_MIXTRAL_22B.getValue())
                .withTemperature(1f)
                .withTopP(1F)
                .build());

        ChatResponse response = chatModel.call(
                new Prompt("""
                                                   
                                   Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation spécialisée, travail social, psychomotricité et intégration sensorielle ; le tout dans le domaine de l'autisme. Tu as établi un programme pour un autiste et un ensemble de leçons.\s
                                                   
                                   Le système a été pensé ainsi : à partir des informations sur la leçon du jour, tu crées des scénarios de conversation. D'abord, tu décides du scénario et de ce qu'il aborde au sujet de la leçon. Après l'avoir décidé, tu génères en fonction du type de scenario demande un nouveau scenario. dans le scenario tu simules une conversation entre deux ou plusieurs personnes dont l autiste et l autiste doit completer la conversation.
                                    le scenario peut etre de type : INIT_CONV (initier une conversation), END_CONV(terminer une conversation), CONTINUE_CONV (continuer une conversation afin d apprendre a converser)            
                                   Voici où nous en sommes :
                                   Nous sommes sur la leçon """ + lecon.getNom() + """
                                   parlant de """ + lecon.getDescription() + """
                                   avec pour objectif """ + lecon.getObjSpec() + """
                                               
                                           Le scénario est composé d'une image qui sera envoyée avec un contexte. L'utilisateur est un autiste pour lui apprendre cette leçon.
                                           Tu vas décrire 5 scénarios portant sur la leçon. Chaque scénario est composé des champs suivants :
                                           - aiConv : "la simulation de la conversation : en json avec pour valeur les intervenants; le champ correspondant a la reponse attendue de l autiste est you et a pour valeur "blank" ";
                                           - communicationType : "le type du scenario";
                                           - contexte : "le contexte de cette discussion";
                                           - name : "le nom du scénario";
                                           - description : "la description du scénario";
                                           Attention :
                                           Le résultat doit être au format JSON avec uniquement les informations demandées. Ne répond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées. N'oublie pas de remplir tous les champs.
                                           
                                           Exemple de format JSON attendu :
                                           [
                                             {
                                             name: "...",
                                             description: "...",
                                               "aiConv": "...",
                                               "communicationType": "...",
                                               "contexte": "...",
                                               
                                             },
                                             ...
                                           ]
                                                           
                                   """
                ));

        try {

            String resp = response.getResult().getOutput().getContent();
            JSONArray jsonArray = new JSONArray(resp);
            List<Communication> communications = new ArrayList<>();
            jsonArray.forEach(jsonElement -> {
                JSONObject contentJson = (JSONObject) jsonElement;
                Communication communication = new Communication();
                communication.setName(contentJson.getString("name"));
                communication.setDescription(contentJson.getString("description"));

                communication.setAiConv(contentJson.getString("aiConv"));
                communication.setContexte(contentJson.getString("contexte"));
                String type = contentJson.getString("communicationType");
                switch (type) {
                    case "INIT_CONV":
                        communication.setCommunicationType(CommunicationType.INIT_CONV);
                        break;

                    case "END_CONV":

                        communication.setCommunicationType(CommunicationType.END_CONV);
                        break;

                    case "CONTINUE_CONV":

                        communication.setCommunicationType(CommunicationType.CONTINUE_CONV);
                        break;


                }
                communications.add(communication);

            });
            return communications;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
