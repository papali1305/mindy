package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.dto.LeconDto;
import com.enspd.mindyback.models.Lecon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.enspd.mindyback.config.Utils.LECON_ENDPOINT;

@Schema(description = "lecon de chapitres")
public interface LeconApi {

    @PostMapping(LECON_ENDPOINT + "/createLecons")
    @Operation(summary = "creer les lecons du chapitre (6) si celui ci est debloque")
    @ApiResponse(responseCode = "200", description = "Lecons cree", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeconDto.class)))
    public List<LeconDto> createChapterLecons(@RequestBody Integer chapterId, @RequestHeader(name = "Authorization") String jwt);


    @GetMapping(LECON_ENDPOINT + "/findAll/{chapterId}")
    public List<LeconDto> findChapterLecons(@PathVariable("chapterId") Integer chapterId);

    @GetMapping(LECON_ENDPOINT + "/findById/{id}")
    public LeconDto findLeconById(@PathVariable("id") Integer id);


    @DeleteMapping(LECON_ENDPOINT + "/delete/{id}")
    public void deleteLecon( @PathVariable("id") Integer id);

    @PutMapping(LECON_ENDPOINT + "/update")
    public Lecon UpdateLecon(@RequestBody LeconDto lecon, @RequestHeader(name = "Authorization") String jwt);

}
