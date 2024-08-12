package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.CompetenceDto;
import com.enspd.mindyback.dto.GameResponseDto;
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
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class IaServiceImpl2 implements IaService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ScenarioRepository scenarioRepository;
    @Autowired
    private CommunicationRepository communicationRepository;

    @Autowired
    private ChatLanguageModel geminiChatModel;

    @Value("${spring.ai.huggingface.api-key}")
    private String huggingFaceToken ;

    @Value("${spring.ai.huggingface.stabilityai.base-url}")
    private String huggingFacStabilityurl ;

    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
                .project("mindy-432210")
                .location("northamerica-northeast1")
                .modelName("gemini-1.5-flash-001")
                .maxRetries(3)
                .build();
        System.out.println(model.generate("Hello, how are you?"));
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
                throw new InvalidOperationException("impossible d enregistrer l image", ErrorCodes.IMAGE_SAVE_ERROR);
            }
        } else {
            throw new InvalidOperationException("l'ia n a pas pu generer la scène", ErrorCodes.IA_RESPONSE_NOT_FOUND);
        }
    }


    @Override
    public List<Scenario> createScenarios(Lecon lecon, UserDto user) {

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(
                new SystemMessage("""
                                                          
                                          Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation spécialisée, travail social, psychomotricité et intégration sensorielle ; le tout dans le domaine de l'autisme. Tu as établi un programme pour un autiste et un ensemble de leçons.\s
                                                          
                                          Le système a été pensé ainsi : à partir des informations sur la leçon du jour, tu crées des scénarios qui sont des jeux. D'abord, tu décides du scénario et de ce qu'il aborde au sujet de la leçon. Après l'avoir décidé, tu génères une description de la scène qui sera plus tard représentée en une image. Ensuite, tu génères une question sur cette scène. L'autiste répond et tu apprécies sa réponse.
                                                          
                                          Voici où nous en sommes :
                                          Nous sommes sur la leçon """ + lecon.getName() + """
                                          parlant de """ + lecon.getDescription() + """
                                          avec pour objectif """ + lecon.getObjectives() + """
                                                                  
                                                  Le scénario est composé d'une image qui sera envoyée avec un contexte. L'utilisateur est un autiste pour lui apprendre cette leçon.
                                                  Tu vas décrire 8 scénarios portant sur la leçon. Chaque scénario est composé des champs suivants :
                                                  - scenarioName : "le nom du scénario";
                                                  - scenarioDescription : "la description courte du scénario. pas plus de 100 mots";
                                                  - aiQuestion : "la question à poser à l'autiste portant sur la leçon et le scénario que tu as généré. sois court et concis";
                                                  - prompt : "un prompt qui sera envoyé à une IA de génération d'image qui va, à partir de ce prompt, créer une image correspondante à la description du scénario puis sera envoyée à l'utilisateur. 
                                                  Lors de l ecriture du prompt prends en compte que la pluspart des personnes doivent etre noir que l'ia doit mettre l'accent sur certains details de la scene que tu preciseras.";
                                                  - scenarioType : "peut être soit RECOGNIZE_FACIAL_EXPRESSIONS, APPROPRIATE_REACTIONS, UNDERSTAND_EMOTIONS, COMMUNICATION_SKILLS, CONFLICT_RESOLUTION, GROUP_ACTIVITIES, DAILY_LIFE_SKILLS, SCHOOL_WORK_BEHAVIOR".
                                                  - contexte : "une description breve de l image demande / du prompt pas plus de 30 mots";
                                                  Attention :
                                                  Le résultat doit être au format JSON avec uniquement les informations demandées et en langue anglaise. Ne répond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées. N'oublie pas de remplir tous les champs.
                                                  ne repond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées. N'oublie pas de remplir tous les champs.
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
                )
        );


        messages.add(new UserMessage(""" 
                                             '''
                                             comorbidities : """ + user.comorbidities() + """
                                             ;   age : """ + user.age() + """
                                             ;   gender : """ + user.gender() + """
                                             ;   city : """ + user.city() + """
                                             ;   state : """ + user.state() + """
                                             ;   country : """ + user.country() + """
                                             '''
                                             """));


        try {

            Response<AiMessage> response = geminiChatModel.generate(messages);
            String resp = response.content().text();
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


        List<ChatMessage> messages = new ArrayList<>();
        messages.add(
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
                                                  Tu vas décrire 8 scénarios portant sur la leçon. Chaque scénario est composé des champs suivants :
                                                  - aiConv : "la simulation de la conversation un string json . la conversation doit etre la plus humaine possible: 
                                                  en json avec pour champs les intervenants (imagine des noms) et pour valeur des champs leurs repartis;
                                                   le champ correspondant a la reponse attendue de l autiste est you et a pour valeur 'blank'  ";
                                                  - communicationType : "le type du scenario";
                                                  - context : "le contexte de cette discussion c est a dire ou elle a lieu pourquoi elle a lieu comment elle s est mise en place. pas plus de 30 mots";
                                                  - name : "le nom du scénario";
                                                  - description : "la description du scénario";
                                                  Attention :
                                                  Le résultat doit être au format JSON avec uniquement les informations demandées et en langue anglaise. Ne répond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées. N'oublie pas de remplir tous les champs.
                                                  
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
                                                                  
                                          """));
        messages.add(
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
                ));


        try {

            Response<AiMessage> response = geminiChatModel.generate(messages);
            String resp = response.content().text();
            JSONArray jsonArray = new JSONArray(resp);

            List<Communication> communications = new ArrayList<>();

            jsonArray.forEach(jsonElement -> {
                JSONObject contentJson = (JSONObject) jsonElement;
                Communication communication = new Communication();
                communication.setName(contentJson.getString("name"));
                communication.setDescription(contentJson.getString("description"));

                communication.setAiConv(contentJson.getString("aiConv"));
                communication.setContext(contentJson.getString("context"));
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


        List<ChatMessage> messages = new ArrayList<>();
        messages.add(
                new SystemMessage("""

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
                                            
                                           Tu vas décrire 10 lecons portant sur le chapitre .Chaque lecon est composé des champs suivants:
                                                
                                           -  name : le nom de la lecon pas plus de 10 mots;
                                                                     
                                           - description : la description de la lecon pas plus de 100 mots;
                                                                     
                                           - objectives : les objectifs specifiques de la lecon pas plus de 100 mots;
                                          Attention:
                                          Le résultat doit être au format JSON avec uniquement les informations demandées et en langue anglaise.Ne répond que par le JSON
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
                ));
        messages.add(
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
                ));

        try {

            Response<AiMessage> response = geminiChatModel.generate(messages);
            String resp = response.content().text();
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
    public Correction corrigeGame(Integer gameId, GameResponseDto userResponse) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Aucun jeu trouvé avec l id " + gameId));
        game.setUserResponse(userResponse.useresponse());
        gameRepository.save(game);


        List<ChatMessage> messages = new ArrayList<>();

        switch (game.getType()) {
            case SCENARIO:
                Scenario scenario = scenarioRepository.findById(game.getId()).orElseThrow(() -> new RuntimeException("Aucun scénario trouvé avec l id " + game.getId()));
                //   System.out.println(scenario);

                messages.add(
                        new UserMessage(
                                """ 
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
                                        """));
                break;
            case COMMUNICATION:
                Communication communication = communicationRepository.findById(game.getId()).orElseThrow(() -> new RuntimeException("Aucun communication trouvé avec l id " + game.getId()));
                messages.add(
                        new UserMessage("""

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
                                                """));
                break;
        }


        try {

            Response<AiMessage> response = geminiChatModel.generate(messages);
            String resp = response.content().text();
            JSONObject jsonObject = new JSONObject(resp);

            Correction correction = new Correction();
            correction.setAnalysis(jsonObject.getString("analysis"));
            correction.setResponse(jsonObject.getString("response"));
            correction.setCorrect(jsonObject.getBoolean("isCorrect"));

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

        UserDto userDto = competenceDto.user();

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(
                new SystemMessage("""
                          Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation
                          spécialisée, travail social, psychomotricité et intégration sensorielle;
                          le tout dans le domaine de l 'autisme. Tu vas établir un programme pour un autiste, un ensemble de chapitres. portant sur la competence que celui ci va te demander
                         Le système a été pensé ainsi :à partir des informations sur l autiste qui est l utilisateur et sur la competence souhaite, tu crées un programme de chapitres progressifs
                         Le message de l utilisateur commence avec des triples backtips ''' .
                         Tu repondras avec un ensemble de 25 chapitres constitue chacun d'eux des informations suivantes :
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
                        Ne répond que par le JSON des chapitres, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées et en langue anglaise n'oublie pas de remplir tous les champs.
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
                ));
        messages.add(
                new UserMessage(" ''' competenceType : " + competenceDto.competenceType().getName()
                                + "; severityLevel : " + userDto.severityLevel()
                                + "; age : " + userDto.age()
                                + "; gender : " + userDto.gender()
                                + "; city : " + userDto.city()
                                + "; state : " + userDto.state()
                                + "; country:" + userDto.country()
                                + " ; comorbidities : " + userDto.comorbidities() + ";  ''' ")
        );

        try {

           Response<AiMessage> response = geminiChatModel.generate(messages);
            String resp = response.content().text();
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

    @Override
    public List<SentenceQcm> createSentenceQcms(Lecon lecon, UserDto user) {


        List<ChatMessage> messages = new ArrayList<>();
        messages.add(
                new SystemMessage("""
                                                                    
                                                    Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation spécialisée, travail social, psychomotricité et intégration sensorielle ; le tout dans le domaine de l'autisme. Tu as établi un programme pour un autiste et un ensemble de leçons.\s
                                                                    
                                                    Le système a été pensé ainsi : à partir des informations sur la leçon du jour, tu crees une phrase incomplete qui manque d un ou plusieurs mots et tu remplaceras ces mots manquants par le mot 'blank' . ensuite tu donneras des propositinos de mots manquants fausses mais proche de ceux manquants, puis tu donneras les mots manquants.
                                                    l autiste doit ensuite corriger cette phrase en choisissant parmis les mots proposes ceux qui sont vrai des faux. le but de l'exercice est de faire progresser les competences vervales de l autiste .
                                                     A partir des informations sur l autiste et sur la lecon, tu crées des une series de scenarios adaptes a l autiste a partir des informations envoyes par l utilisateur.
                                                  les informations sur l autiste seront entres  par l utilisateur et entre les triples backtips ''' .
                                                                                                
                                                    Voici où nous en sommes :
                                                    Nous sommes sur la leçon """ + lecon.getName() + """
                                                  parlant de """ + lecon.getDescription() + """
                                                  avec pour objectif """ + lecon.getObjectives() + """
                                                                      
                                                                   L'utilisateur est un autiste pour lui apprendre cette leçon;
                                                                  Tu vas décrire 8 scenarios portant sur la leçon. Chaque scénario est composé des champs suivants :
                                                                  - sentenceToComplete : "la phrase incomplete à corriger par l autiste . cette phrase doit porter sur la leçon du jour et sur le sujet de la leçon et les mots manquants doivent être remplacés par le mot 'blank' ";
                                                                   le champ correspondant a la reponse attendue de l autiste est you et a pour valeur 'blank'  ";
                                                                  - words : "la liste des mots proposes qui sont faux. pas plus de 8 mots . ce sera un tableau de mots";
                                                                   - response : "la liste des mots proposes qui sont vrai. ce sera un tableau de mots";
                                                                  Attention :
                                                                  Le résultat doit être au format JSON avec uniquement les informations demandées. Ne répond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées et en langue anglaise. N'oublie pas de remplir tous les champs.
                                                                  
                                                                  Exemple de format JSON attendu :
                                                                  [
                                                                    {
                                                    "sentenceToComplete": "Le 'blank' de la phrase donne des détails supplémentaires sur le verbe.",
                                                    "words": "["sujet", "verbe", "adjectif", "article", "préposition", "pronom", "adverbe", "conjonction"]",
                                                    "response": "["complément"]"
                                                  },
                                                                    ...
                                                                  ]
                                                                                  
                                                          """));
        messages.add(
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
                        ));


        try {

            Response<AiMessage> response = geminiChatModel.generate(messages);
            String resp = response.content().text();
            JSONArray jsonArray = new JSONArray(resp);

            List<SentenceQcm> sentenceQcms = new ArrayList<>();

            jsonArray.forEach(jsonElement -> {
                JSONObject contentJson = (JSONObject) jsonElement;
                SentenceQcm sentenceQcm = new SentenceQcm();
                sentenceQcm.setName(contentJson.getString("sentenceToComplete"));

                sentenceQcm.setResponse(contentJson.getString("response"));

                sentenceQcm.setWords(contentJson.getString("words"));

                sentenceQcm.setType(GameType.SENTENCE_QCM);
                sentenceQcms.add(sentenceQcm);

            });
            return sentenceQcms;

        } catch (JSONException e) {
            throw new InvalidEntityException("La reponse de l'IA n'est pas valide et ne peut pas être traitée en json", ErrorCodes.IA_RESPONSE_NOT_VALID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidOperationException("L'IA ne répond pas", ErrorCodes.IA_RESPONSE_NOT_FOUND);
        }
    }


    @Override
    public List<SentenceCompletion> createSentenceCompletions(Lecon lecon, UserDto user) {


        List<ChatMessage> messages = new ArrayList<>();
        messages.add(
                new SystemMessage("""
                                                                    
                                                    Tu es un expert qui maîtrise les sujets suivants : Psychiatrie, psychologie, neurologie, orthophonie, ergothérapie, analyse des comportements certifiés, éducation spécialisée, travail social, psychomotricité et intégration sensorielle ; le tout dans le domaine de l'autisme. Tu as établi un programme pour un autiste et un ensemble de leçons.\s
                                                                    
                                                    Le système a été pensé ainsi : à partir des informations sur la leçon du jour, tu crees une phrase incomplete qui manque d un ou plusieurs mots et tu remplaceras ces mots manquants par le mot 'blank' .
                                                    l autiste doit ensuite corriger cette phrase en lui envoyant le ou les mots manquants. le but de l'exercice est de faire progresser les competences vervales de l autiste.
                                                    La phrase doit porte sur la lecon du jour et sur le sujet de la leçon appelons cette phrase un scenario.
                                                     A partir des informations sur l autiste et sur la lecon, tu crées des une series de scenarios adaptes a l autiste a partir des informations envoyes par l utilisateur.
                                                  les informations sur l autiste seront entres  par l utilisateur et entre les triples backtips ''' .
                                                                                                
                                                    Voici où nous en sommes :
                                                    Nous sommes sur la leçon """ + lecon.getName() + """
                                                  parlant de """ + lecon.getDescription() + """
                                                  avec pour objectif """ + lecon.getObjectives() + """
                                                              
                                                           L'utilisateur est un autiste pour lui apprendre cette leçon;
                                                          Tu vas décrire 8 scenarios portant sur la leçon. Chaque scénario est composé des champs suivants :
                                                          - sentenceToComplete : "la phrase incomplete à corriger par l autiste . cette phrase doit porter sur la leçon du jour et sur le sujet de la leçon et les mots manquants doivent être remplacés par le mot 'blank' ";
                                                           le champ correspondant a la reponse attendue de l autiste est you et a pour valeur 'blank'  ";
                                                          - context : "le contexte dans lequel s inscrit cette phrase c est a dire ou elle a lieu pourquoi elle a lieu comment elle s est mise en place. pas plus de 30 mots";
                                                                                                    
                                                          Attention :
                                                          Le résultat doit être au format JSON avec uniquement les informations demandées et en langue anglaise.  Ne répond que par le JSON des scénarios, rien d'autre, aucun commentaire. Ta réponse doit pouvoir être convertie en JSON avec uniquement les informations demandées. N'oublie pas de remplir tous les champs.
                                                          
                                                          Exemple de format JSON attendu :
                                                          [
                                                      
                                                             {
                                                        "sentenceToComplete": "Le 'blank' de la phrase donne des détails supplémentaires sur le verbe. et le 'blank' du verbe donne des détails supplémentaires sur le complément.",
                                                        "response": ["complément", "verbe"]
                                                      },
                                                            ...
                                                          ]
                                                                          
                                                  """));
        messages.add(
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
                        ));


        try {

            Response<AiMessage> response = geminiChatModel.generate(messages);
            String resp = response.content().text();
            JSONArray jsonArray = new JSONArray(resp);

            List<SentenceCompletion> sentenceCompletions = new ArrayList<>();

            jsonArray.forEach(jsonElement -> {
                JSONObject contentJson = (JSONObject) jsonElement;
                SentenceCompletion sentenceCompletion = new SentenceCompletion();
                sentenceCompletion.setName(contentJson.getString("sentenceToComplete"));

                sentenceCompletion.setContext(contentJson.getString("context"));


                sentenceCompletion.setType(GameType.SENTENCE_COMPLETION);
                sentenceCompletions.add(sentenceCompletion);

            });
            return sentenceCompletions;

        } catch (JSONException e) {
            throw new InvalidEntityException("La reponse de l'IA n'est pas valide et ne peut pas être traitée en json", ErrorCodes.IA_RESPONSE_NOT_VALID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidOperationException("L'IA ne répond pas", ErrorCodes.IA_RESPONSE_NOT_FOUND);
        }
    }

}
