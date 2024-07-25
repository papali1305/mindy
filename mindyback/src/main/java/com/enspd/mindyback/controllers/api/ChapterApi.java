package com.enspd.mindyback.controllers.api;


import com.enspd.mindyback.dto.ChapterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static com.enspd.mindyback.config.Utils.CHAPTER_ENDPOINT;

@Schema(description = "Chapitres de competences", name = "Chapter_Api")
public interface ChapterApi {

    @GetMapping(CHAPTER_ENDPOINT + "/findById/{id}")
    @Operation(summary = "Recuperer un chapitre par son id", description = "Recuperer un chapitre par son id ")
    @ApiResponse(responseCode = "200", description = "chapitre recupere" ,  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChapterDto.class)))
    @ApiResponse(responseCode = "3000", description = "Erreur chapitre non trouve")
    @ApiResponse(responseCode = "3001", description = "Erreur chapitre non valides pour la sortie")
    public ChapterDto getChapterById(@PathVariable(value = "id") Integer id ,  @RequestHeader(name = "Authorization") String jwt);

    @GetMapping(CHAPTER_ENDPOINT + "/findAll")
    @Operation(summary = "Recuperer tous les chapitres", description = "Recuperer tous les chapitres")
    public List<ChapterDto> getAllChapter(@RequestHeader(name = "Authorization") String jwt);

    @GetMapping(CHAPTER_ENDPOINT + "/findAllCompetence/{idCompetence}")
    @ApiResponse(responseCode = "200", description = "chapitres recuperes" ,  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChapterDto.class)))
    @ApiResponse(responseCode = "3000", description = "Erreur aucun chapitre trouve pour cette competence")
    @ApiResponse(responseCode = "3001", description = "Erreur chapitres non valides pour la sortie")

    @Operation(summary = "Recuperer tous les chapitres d'une competence", description = "Recuperer tous les chapitres d'une competence")
    public List<ChapterDto> getAllChapterByCompetence(@PathVariable(value = "idCompetence") Integer idCompetence , @RequestHeader(name = "Authorization") String jwt);

    @PutMapping(CHAPTER_ENDPOINT + "/updateChapterCurrent/{id}")
    @ApiResponse(responseCode = "200", description = "Faire passer ce chapitre comme le nouveau chapitre courrent effectue" ,  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChapterDto.class)))
    @ApiResponse(responseCode = "3003", description = "Erreur chapitres n a pas pu etre change pour courant")
    @Operation(summary = "maj le chapitre courrant ; le chapitre sera marque comme courrant", description = "Valider un chapitre, le chapitre sera marque comme complete")

    public void updateChapterCurrent(Integer id);

    @PutMapping(CHAPTER_ENDPOINT + "/validateChapter/{id}")
    @Operation(summary = "Valider un chapitre ; le chapitre sera marque comme complete", description = "Valider un chapitre, le chapitre sera marque comme complete")
    @ApiResponse(responseCode = "200", description = "Faire passer ce chapitre comme le nouveau chapitre courrent effectue" ,  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChapterDto.class)))
    @ApiResponse(responseCode = "3002", description = "Erreur chapitre n a pas pu etre valide")

    public void validateChapter(Integer id);
}
