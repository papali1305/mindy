package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.dto.CompetenceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.enspd.mindyback.config.Utils.COMPETENCE_ENDPOINT;


@Schema(description = " les competences abordes.")
public interface CompetenceApi {


    @PostMapping(  COMPETENCE_ENDPOINT + "/create")
    CompetenceDto createCompetence(@RequestBody CompetenceDto competenceDto, @RequestHeader(name = "Authorization") String jwt);

    @PutMapping(  COMPETENCE_ENDPOINT + "/update")
    CompetenceDto updateCompetence(@RequestBody CompetenceDto competenceDto, @RequestHeader(name = "Authorization") String jwt);

    @GetMapping(  COMPETENCE_ENDPOINT + "/findById/{id}")
    CompetenceDto getCompetence(@PathVariable("id") Integer id ,  @RequestHeader(name = "Authorization") String jwt);

    @GetMapping(  COMPETENCE_ENDPOINT + "/findAllByUserId")
    @Operation(summary = "Recuperer toutes les competences de l utilisateur ( 3 ) ")
    @ApiResponse(responseCode = "200", description = "Competences de l utilisateur trouvées" ,  content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompetenceDto.class)))
    @ApiResponse(responseCode = "2000", description = "Erreur competences non trouves")
    @ApiResponse(responseCode = "2001", description = "Erreur competences non valides pour la sortie")
    List<CompetenceDto> getAllCompetences( @RequestHeader(name = "Authorization") String jwt);
}
