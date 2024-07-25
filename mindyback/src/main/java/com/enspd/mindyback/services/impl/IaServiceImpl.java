package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.ChapterDto;
import com.enspd.mindyback.dto.CompetenceDto;
import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.exception.InvalidEntityException;
import com.enspd.mindyback.exception.InvalidOperationException;
import com.enspd.mindyback.models.*;
import com.enspd.mindyback.models.type.CommunicationType;
import com.enspd.mindyback.models.type.GameType;
import com.enspd.mindyback.models.type.ScenarioType;
import com.enspd.mindyback.repository.CommunicationRepository;
import com.enspd.mindyback.repository.GameRepository;
import com.enspd.mindyback.repository.ScenarioRepository;
import com.enspd.mindyback.services.IaService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.ai.mistralai.MistralAiChatOptions;
import org.springframework.ai.mistralai.api.MistralAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IaServiceImpl implements IaService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ScenarioRepository scenarioRepository;
    @Autowired
    private CommunicationRepository communicationRepository;

    private String GeminiToken = "AIzaSyBySOfAajgwYn5MPjoChfnkfCga1WsPGQs";

    private String GeminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest";

    private String GeminiModel = "text-davinci-003";

    private String huggingFaceToken = "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";

    private String mistralAiToken = "rj95jKUICS5sSOHBEt1R6YL1NAIuXAp0";

    private String huggingFacStabilityurl = "https://api-inference.huggingface.co/models/stabilityai/stable-diffusion-3-medium-diffusers";
    private String huggingFaceLLAMA3url = "https://api-inference.huggingface.co/models/meta-llama/Meta-Llama-3-8B-Instruct/v1/chat/completions";


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
                throw new InvalidOperationException("impossible d enregistrer l image", ErrorCodes.IMAGE_SAVE_ERROR);
            }
        } else {
            throw new InvalidOperationException("l'ia n a pas pu generer la scène", ErrorCodes.IA_RESPONSE_NOT_FOUND);
        }
    }


    @Override
    public List<Scenario> createScenarios(Lecon lecon, UserDto user) {

        var mistralAiApi = new MistralAiApi(mistralAiToken);

        var chatModel = new MistralAiChatModel(mistralAiApi, MistralAiChatOptions.builder()
                .withModel(MistralAiApi.ChatModel.OPEN_MIXTRAL_22B.getValue())
                .withTemperature(1f)
                .withTopP(1F)
                .build());

        ChatResponse response = chatModel.call(
                new Prompt(Arrays.asList(
                        new SystemMessage(
                                """
                                                        
                                        Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation spécialisée, travail social, psychomotricité et intégration sensorielle ; le tout dans le domaine de l'autisme. Tu as établi un programme pour un autiste et un ensemble de leçons.\s
                                                        
                                        Le système a été pensé ainsi : à partir des informations sur la leçon du jour, tu crées des scénarios qui sont des jeux. D'abord, tu décides du scénario et de ce qu'il aborde au sujet de la leçon. Après l'avoir décidé, tu génères une description de la scène qui sera plus tard représentée en une image. Ensuite, tu génères une question sur cette scène. L'autiste répond et tu apprécies sa réponse.
                                                        
                                        Voici où nous en sommes :
                                        Nous sommes sur la leçon """ + lecon.getName() + """
                                        parlant de """ + lecon.getDescription() + """
                                        avec pour objectif """ + lecon.getObjectives() + """
                                                                
                                                Le scénario est composé d'une image qui sera envoyée avec un contexte. L'utilisateur est un autiste pour lui apprendre cette leçon.
                                                Tu vas décrire 10 scénarios portant sur la leçon. Chaque scénario est composé des champs suivants :
                                                - scenarioName : "le nom du scénario";
                                                - scenarioDescription : "la description courte du scénario. pas plus de 100 mots";
                                                - aiQuestion : "la question à poser à l'autiste portant sur la leçon et le scénario que tu as généré. sois court et concis";
                                                - prompt : "un prompt qui sera envoyé à une IA de génération d'image qui va, à partir de ce prompt, créer une image correspondante à la description du scénario puis sera envoyée à l'utilisateur. 
                                                Lors de l ecriture du prompt prends en compte que la pluspart des personnes doivent etre noir que l'ia doit mettre l'accent sur certains details de la scene que tu preciseras.";
                                                - scenarioType : "peut être soit RECOGNIZE_FACIAL_EXPRESSIONS, APPROPRIATE_REACTIONS, UNDERSTAND_EMOTIONS, COMMUNICATION_SKILLS, CONFLICT_RESOLUTION, GROUP_ACTIVITIES, DAILY_LIFE_SKILLS, SCHOOL_WORK_BEHAVIOR".
                                                - contexte : "une description breve de l image demande / du prompt pas plus de 30 mots";
                                                Attention :
                                                Le résultat doit être au format JSON avec uniquement les informations demandées. Ne répond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées. N'oublie pas de remplir tous les champs.
                                                
                                                Exemple de format JSON attendu :
                                                [
                                                  {
                                                    "scenarioName": "Nom du scénario",
                                                    "scenarioDescription": "Description du scénario.",
                                                    "aiQuestion": "Question à poser à l'autiste.",
                                                    "prompt": "Prompt pour l'IA de génération d'image.",
                                                    "scenarioType": "Type de scénario",
                                                    "context": "Description de l image demande / du prompt"
                                                  },
                                                  ...
                                                ]
                                                                
                                        """
                        ), new UserMessage(
                                """ 
                                        '''
                                        comorbidities : """ + user.comorbidities() + """
                                        ;   age : """ + user.age() + """
                                        ;   gender : """ + user.gender() + """
                                        ;   city : """ + user.city() + """
                                        ;   state : """ + user.state() + """
                                        ;   country : """ + user.country() + """
                                        '''
                                        """
                        )
                )));

        try {

            String resp = response.getResult().getOutput().getContent();
            JSONArray jsonArray = new JSONArray(resp);
            List<Scenario> scenarios = new ArrayList<>();
            jsonArray.forEach(jsonElement -> {
                JSONObject contentJson = (JSONObject) jsonElement;
                ScenarioScene scenarioScene = generateScene(contentJson.getString("prompt"));
                scenarioScene.setPrompt(contentJson.getString("context"));
                Scenario scenario = new Scenario();
                scenario.setName(contentJson.getString("scenarioName"));
                scenario.setAiQuestion(contentJson.getString("aiQuestion"));
                scenario.setDescription(contentJson.getString("scenarioDescription"));
                scenarioScene.setScenario(scenario);

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
                scenario.setType(GameType.SCENARIO);

                scenarios.add(scenario);

            });
            return scenarios;

        } catch (JSONException e) {
            throw new InvalidEntityException("La reponse de l'IA n'est pas valide et ne peut pas être traitée en json", ErrorCodes.IA_RESPONSE_NOT_VALID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidOperationException("L'IA ne répond pas", ErrorCodes.IA_RESPONSE_NOT_FOUND);
        }

    }

    @Override
    public List<Communication> createCommunication(Lecon lecon, UserDto user) {

        var mistralAiApi = new MistralAiApi(mistralAiToken);

        var chatModel = new MistralAiChatModel(mistralAiApi, MistralAiChatOptions.builder()
                .withModel(MistralAiApi.ChatModel.OPEN_MIXTRAL_22B.getValue())
                .withTemperature(.8f)
                .withTopP(1F)
                .build());

        ChatResponse response = chatModel.call(
                new Prompt(Arrays.asList(
                        new SystemMessage("""
                                                                    
                                                    Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation spécialisée, travail social, psychomotricité et intégration sensorielle ; le tout dans le domaine de l'autisme. Tu as établi un programme pour un autiste et un ensemble de leçons.\s
                                                                    
                                                    Le système a été pensé ainsi : à partir des informations sur la leçon du jour, tu crées des scénarios de conversation. D'abord, tu décides du scénario et de ce qu'il aborde au sujet de la leçon. Après l'avoir décidé, tu génères en fonction du type de scenario demande un nouveau scenario. dans le scenario tu simules une conversation entre deux ou plusieurs personnes dont l autiste et l autiste doit completer la conversation.
                                                     le scenario peut etre de type : INIT_CONV (initier une conversation), END_CONV(terminer une conversation), CONTINUE_CONV (continuer une conversation afin d apprendre a converser)            
                                                     A partir des informations sur l autiste et sur le chapitre, tu crées des scenarios adaptes a l autiste a partir des informations envoyes par l utilisateur.
                                                  les informations sur l autiste seront entres  par l utilisateur et entre les triples backtips ''' .
                                                                                                
                                                    Voici où nous en sommes :
                                                    Nous sommes sur la leçon """ + lecon.getName() + """
                                                  parlant de """ + lecon.getDescription() + """
                                                  avec pour objectif """ + lecon.getObjectives() + """
                                                              
                                                           L'utilisateur est un autiste pour lui apprendre cette leçon.
                                                          Tu vas décrire 5 scénarios portant sur la leçon. Chaque scénario est composé des champs suivants :
                                                          - aiConv : "la simulation de la conversation un string json . la conversation doit etre la plus humaine possible: 
                                                          en json avec pour champs les intervenants (imagine des noms) et pour valeur des champs leurs repartis;
                                                           le champ correspondant a la reponse attendue de l autiste est you et a pour valeur 'blank'  ";
                                                          - communicationType : "le type du scenario";
                                                          - context : "le contexte de cette discussion c est a dire ou elle a lieu pourquoi elle a lieu comment elle s est mise en place. pas plus de 30 mots";
                                                          - name : "le nom du scénario";
                                                          - description : "la description du scénario";
                                                          Attention :
                                                          Le résultat doit être au format JSON avec uniquement les informations demandées. Ne répond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées. N'oublie pas de remplir tous les champs.
                                                          
                                                          Exemple de format JSON attendu :
                                                          [
                                                            {
                                                            "name": "...",
                                                            "description": "...",
                                                            "aiConv": "...",
                                                            "communicationType": "...",
                                                            "context": "...",
                                                              
                                                            },
                                                            ...
                                                          ]
                                                                          
                                                  """),
                        new UserMessage(
                                """ 
                                        '''
                                        comorbidities : """ + user.comorbidities() + """
                                        ;   age : """ + user.age() + """
                                        ;   gender : """ + user.gender() + """
                                        ;   city : """ + user.city() + """
                                        ;   state : """ + user.state() + """
                                        ;   country : """ + user.country() + """
                                        '''
                                        """
                        )

                )));

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
                communication.setContext(contentJson.getString("contexte"));
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
                communication.setType(GameType.COMMUNICATION);
                communications.add(communication);

            });
            return communications;

        } catch (JSONException e) {
            throw new InvalidEntityException("La reponse de l'IA n'est pas valide et ne peut pas être traitée en json", ErrorCodes.IA_RESPONSE_NOT_VALID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidOperationException("L'IA ne répond pas", ErrorCodes.IA_RESPONSE_NOT_FOUND);
        }
    }

    @Override
    public List<Lecon> createLecons(Chapter chapter, UserDto user) {

        var mistralAiApi = new MistralAiApi(mistralAiToken);

        var chatModel = new MistralAiChatModel(mistralAiApi, MistralAiChatOptions.builder()
                .withModel(MistralAiApi.ChatModel.OPEN_MIXTRAL_22B.getValue())
                .withTemperature(.8f)
                .withTopP(1F)
                .build());

        ChatResponse response = chatModel.call(
                new Prompt(
                        Arrays.asList(
                                new SystemMessage(
                                        """

                                                 Tu es un expert qui maîtrise les sujets suivants :
                                                Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation
                                                spécialisée, travail social, psychomotricité et intégration sensorielle;
                                                le tout dans le domaine de l 'autisme. Tu as établi un programme pour un autiste et un ensemble de chapitres.

                                                Le système a été pensé ainsi :à partir des informations sur le chapitre et sur l autiste, tu crées des
                                                lecons progressives.
                                                A partir des informations sur l autiste et sur le chapitre, tu crées des lecons progressives avec des sujets de plus en plus avances et progressifs et adaptes a l autiste a partir des informations envoyes par l utilisateur.
                                                les informations sur l autiste seront entres  par l utilisateur et entre les triples backtips ''' .
                                                Voici les informations sur le chapitre :
                                                                                   
                                                -""" + chapter.getName() + """
                                                 :"le nom du chapitre";
                                                - """ + chapter.getDescription() + """
                                                 :"la description du chapitre";
                                                - """ + chapter.getObjectives() + """
                                                      :"le objectif du chapitre";
                                                  
                                                 Tu vas décrire 9 lecons portant sur le chapitre .Chaque lecon est composé des champs suivants:
                                                      
                                                 -  name : le nom de la lecon pas plus de 10 mots;
                                                                           
                                                 - description : la description de la lecon pas plus de 100 mots;
                                                                           
                                                 - objectives : les objectifs specifiques de la lecon pas plus de 100 mots;
                                                Attention:
                                                Le résultat doit être au format JSON avec uniquement les informations demandées.Ne répond que par le JSON
                                                des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées n'oublie pas de remplir tous les champs.

                                                              Exemple de format JSON attendu :
                                                     [{
                                                          nom: "...",
                                                          description:"...",
                                                          objectives:"..."
                                                      },
                                                      ...
                                                     ]

                                                 """
                                )
                                ,
                                new UserMessage(
                                        """ 
                                                '''
                                                comorbidities : """ + user.comorbidities() + """
                                                ;   age : """ + user.age() + """
                                                ;   gender : """ + user.gender() + """
                                                ;   city : """ + user.city() + """
                                                ;   state : """ + user.state() + """
                                                ;   country : """ + user.country() + """
                                                '''
                                                """
                                )
                        )));

        try {

            String resp = response.getResult().getOutput().getContent();
            JSONArray jsonArray = new JSONArray(resp);
            List<Lecon> lecons = new ArrayList<>();
            jsonArray.forEach(jsonElement -> {
                JSONObject contentJson = (JSONObject) jsonElement;
                Lecon lecon = new Lecon();
                lecon.setName(contentJson.getString("name"));
                lecon.setDescription(contentJson.getString("description"));

                lecon.setObjectives(contentJson.getString("objectives"));

                lecons.add(lecon);

            });
            return lecons;

        } catch (JSONException e) {
            throw new InvalidEntityException("La reponse de l'IA n'est pas valide et ne peut pas être traitée en json", ErrorCodes.IA_RESPONSE_NOT_VALID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidOperationException("L'IA ne répond pas", ErrorCodes.IA_RESPONSE_NOT_FOUND);
        }
    }

    @Override
    public Correction corrigeGame(Integer gameId, String userResponse) {
        System.out.println(gameId);
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Aucun jeu trouvé avec l id " + gameId));
        game.setUserResponse(userResponse);
        gameRepository.save(game);
        //    System.out.println(game);


        var mistralAiApi = new MistralAiApi(mistralAiToken);

        var chatModel = new MistralAiChatModel(mistralAiApi, MistralAiChatOptions.builder()
                .withModel(MistralAiApi.ChatModel.OPEN_MIXTRAL_22B.getValue())
                .withTemperature(1f)
                .withTopP(1F)
                .build());
        String prompt = null;
        switch (game.getType()) {
            case SCENARIO:
                Scenario scenario = scenarioRepository.findById(game.getId()).orElseThrow(() -> new RuntimeException("Aucun scénario trouvé avec l id " + game.getId()));
                //   System.out.println(scenario);

                prompt = """
                                  Tu es un expert qui maîtrise les sujets suivants :
                                  Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation
                                  spécialisée, travail social, psychomotricité et intégration sensorielle;
                                  le tout dans le domaine de l 'autisme.
                                  Tu as établi un programme pour un autiste et un exercice sous forme de jeu c est le moment de corriger l'exercice.\s

                                  L exercice a ete pense ainsi tu as donne une image avec un contexte de l image et tu as pose une question relative a la question et a l'image a l autiste; celui ci va te repondre  et tu analyseras sa reponse ensuite le corrigeras.
                                  Voici les informations sur l'exercice :

                                 -""" + scenario.getName() + """
                                  :"le nom de l'exercice";
                                 - """ + scenario.getDescription() + """
                                  :"la description de l'exercice";
                                 - """ + scenario.getType() + """
                                  : c est le type de l'exercice; il peut etre soit RECOGNIZE_FACIAL_EXPRESSIONS, APPROPRIATE_REACTIONS, UNDERSTAND_EMOTIONS, COMMUNICATION_SKILLS, CONFLICT_RESOLUTION, GROUP_ACTIVITIES, DAILY_LIFE_SKILLS, SCHOOL_WORK_BEHAVIOR ;
                                 - """ + scenario.getAiQuestion() + """
                                 : la question que tu as pose a l autiste;
                                 - """ + scenario.getLecon().getObjectives() + """
                                 : l'objectif de l'exercice;
                                 - """ + scenario.getScenarioScene().getPrompt() + """
                                  : la description de l image en question
                                 Voici la reponse que l autiste a donne a la question : '''
                                 - """ + scenario.getUserResponse() + """
                                                                                   '''.
                                  Corrige l' exercice en generant un json comportant les champs suivants:

                                  -  analysis : une analyse de la reponse de l autiste. pas plus de 30 mots;
                                  -  response : la reponse corrigee par l autiste. pas plus de 30 mots;
                                  - isCorrect : un booléen qui indique si l autiste a repondu correctement ou pas. true si il a repondu correctement, false sinon;

                                 Attention:
                                 Dans l analyse et la reponse tu t adresses a l autiste donc parles a la 2e personne du singulier.
                                 Le résultat doit être au format JSON avec uniquement les informations demandées.Ne répond que par le JSON
                                 des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées n'oublie pas de remplir tous les champs.
                                 Exemple de format JSON attendu :
                                  {
                                    analysis: "...",
                                    response:"...",
                                    isCorrect: "..."
                                  }
                                 """;
                break;
            case COMMUNICATION:
                Communication communication = communicationRepository.findById(game.getId()).orElseThrow(() -> new RuntimeException("Aucun communication trouvé avec l id " + game.getId()));
                prompt = """

                                         Tu es un expert qui maîtrise les sujets suivants :
                                 Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation
                                 spécialisée, travail social, psychomotricité et intégration sensorielle;
                                 le tout dans le domaine de l 'autisme.
                                 Tu as établi un programme pour un autiste et un exercice sous forme de jeu c est le moment de corriger l'exercice.\s

                                 L exercice a ete pense ainsi : tu as décides du scénario et de ce qu'il aborde au sujet de la leçon. Après l'avoir décidé, tu as génères en fonction du type de scenario demande un nouveau scenario. dans le scenario tu as simules une conversation entre deux ou plusieurs personnes dont l autiste et l autiste doit completer la conversation.
                                    le scenario peut etre de type : INIT_CONV (initier une conversation), END_CONV(terminer une conversation), CONTINUE_CONV (continuer une conversation afin d apprendre a converser)
                                    Voici les informations sur l'exercice :

                                                             -""" + communication.getName() + """
                                                   :"le nom de l'exercice";
                                 - """ + communication.getDescription() + """
                                 :"la description de l'exercice";
                                 - """ + communication.getType() + """
                                 : c est le type de l'exercice; il peut etre soit   INIT_CONV, END_CONV, CONTINUE_CONV ;
                                 - """ + communication.getAiConv() + """
                                 : "la simulation de la conversation : en json avec pour valeur les intervenants; le champ correspondant a la reponse attendue de l autiste est you et a pour valeur "blank" tu remplaceras cela par la reponse que l autiste a donne";
                                 - """ + communication.getContext() + """
                                      : le contexte de la conversation
                                 - """ + communication.getLecon().getObjectives() + """
                                 : l'objectif de l'exercice;

                                 Voici la reponse que l autiste a donne a la question : '''
                                  - """ + communication.getUserResponse() + """
                                     '''.
                                     Corrige l' exercice en generant un json comportant les champs suivants:

                                              -  analysis : une analyse de la reponse de l autiste;
                                     -  response : la reponse corrigee par l autiste.;
                                     - isCorrect : un booléen qui indique si l autiste a repondu correctement ou pas. true si il a repondu correctement, false sinon;
                                     Attention:
                                                                      Dans l analyse et la reponse tu t adresses a l autiste donc parles a la 2e personne du singulier.

                                     Le résultat doit être au format JSON avec uniquement les informations demandées.Ne répond que par le JSON
                                     des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées n'oublie pas de remplir tous les champs.
                                         Exemple de format JSON attendu :
                                 {
                                     analysis: "...",
                                     response:"..."
                                     isCorrect: "..."
                                 }
                                 """;
                break;
        }
        ChatResponse response = chatModel.call(
                new Prompt(prompt));


        try {

            String resp = response.getResult().getOutput().getContent();
            JSONObject jsonObject = new JSONObject(resp);
            System.out.println(jsonObject);
            Correction correction = new Correction();
            correction.setAnalysis(jsonObject.getString("analysis"));
            correction.setResponse(jsonObject.getString("response"));
            correction.setCorrect(jsonObject.getBoolean("isCorrect"));
            System.out.println(correction);
            correction.setGame(game);

            return correction;

        } catch (JSONException e) {
            throw new InvalidEntityException("La reponse de l'IA n'est pas valide et ne peut pas être traitée en json", ErrorCodes.IA_RESPONSE_NOT_VALID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidOperationException("L'IA ne répond pas", ErrorCodes.IA_RESPONSE_NOT_FOUND);
        }
    }

    @Override
    public List<Chapter> createChapters(CompetenceDto competenceDto, UserDto user) {

        var mistralAiApi = new MistralAiApi(mistralAiToken);
        UserDto userDto = competenceDto.user();
        var chatModel = new MistralAiChatModel(mistralAiApi, MistralAiChatOptions.builder()
                .withModel(MistralAiApi.ChatModel.OPEN_MIXTRAL_22B.getValue())
                .withTemperature(1f)
                .withTopP(1F)
                // .withResponseFormat(new MistralAiApi.ChatCompletionRequest.ResponseFormat("json_object"))
                .build());

        Prompt prompt = new Prompt(Arrays.asList(
                new SystemMessage(
                        """
                                  Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation
                                  spécialisée, travail social, psychomotricité et intégration sensorielle;
                                  le tout dans le domaine de l 'autisme. Tu vas établir un programme pour un autiste, un ensemble de chapitres. portant sur la competence que celui ci va te demander
                                 Le système a été pensé ainsi :à partir des informations sur l autiste qui est l utilisateur et sur la competence souhaite, tu crées un programme de chapitres progressifs
                                 Le message de l utilisateur commence avec des triples backtips ''' .
                                 Tu repondras avec un ensemble de 30 chapitres constitue chacun d'eux des informations suivantes :
                                 - name : le nom du chapitre de type string;
                                 - description : la description du chapitre de type string;
                                 - objectives : les objectifs specifiques du chapitre de type string;

                                 l'utilisateur est un autiste pour lui apprendre cette leçon. voici les informations qu il t enverra ignore les champs vides ou nulls:
                                 - competenceType : le type de la competence : il peut etre no verbal competence, verbal competence ou social competence l ' utilisateur t en donnera un et tes chapitres devront portes sur ce sujet;
                                 - severityLevel : le niveau de severite de l autisme : il peut etre mild, modere ou severe;
                                 - age : l age de l autiste;
                                 - gender : le genre de l autiste;
                                 - comorbidities : les comorbidites de l autiste;
                                 - city : la ville de l autiste;
                                 - state : l etat de l autiste;
                                 - country : le pays de l autiste;
                                 Les informations sur l autiste et sur son autisme te servent a concevoir un programme personnalise a celui ci et a son autisme et ses besoins.
                                Ne répond que par le JSON des chapitres, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées n'oublie pas de remplir tous les champs.
                                 voici un exemple de reponse :
                                 [
                                   {
                                     "name": "Nom du chapitre",
                                     "description": "Description du chapitre",
                                     "objectives": "Objectifs du chapitre"
                                   },
                                   ...
                                 ]
                                 """
                ),

                new UserMessage(" ''' competenceType : " + competenceDto.competenceType().getName()
                                + "; severityLevel : " + userDto.severityLevel()
                                + "; age : " + userDto.age()
                                + "; gender : " + userDto.gender()
                                + "; city : " + userDto.city()
                                + "; state : " + userDto.state()
                                + "; country:" + userDto.country()
                                + " ; comorbidities : " + userDto.comorbidities() + ";  ''' ")
        ));

        try {
            ChatResponse response = chatModel.call(prompt);
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            String resp = response.getResult().getOutput().getContent();
            JSONArray jsonArray = new JSONArray(resp);
            List<Chapter> chapters = new ArrayList<>();
            jsonArray.forEach(jsonElement -> {
                JSONObject contentJson = (JSONObject) jsonElement;
                Chapter chapter = new Chapter();
                chapter.setName(contentJson.getString("name"));
                chapter.setDescription(contentJson.getString("description"));
                chapter.setObjectives(contentJson.getString("objectives"));
                chapters.add(chapter);
            });
            return chapters;

        } catch (JSONException e) {
            throw new InvalidEntityException("La reponse de l'IA n'est pas valide et ne peut pas être traitée en json", ErrorCodes.IA_RESPONSE_NOT_VALID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidOperationException("L'IA ne répond pas", ErrorCodes.IA_RESPONSE_NOT_FOUND);
        }
    }

}
