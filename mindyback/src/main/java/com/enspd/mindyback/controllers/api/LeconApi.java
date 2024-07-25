package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.models.Chapter;
import com.enspd.mindyback.models.Lecon;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.enspd.mindyback.config.Utils.LECON_ENDPOINT;

@Schema(description = "lecon de chapitres")
public interface LeconApi {

    @PostMapping(LECON_ENDPOINT + "/createLecons")
    public List<Lecon> createChapterLecons(@RequestBody Integer chapterId, @RequestHeader(name = "Authorization") String jwt);


    @GetMapping(LECON_ENDPOINT + "/findAll/{chapterId}")
    public List<Lecon> findChapterLecons(@PathVariable("chapterId") Integer chapterId);

    @GetMapping(LECON_ENDPOINT + "/findById/{id}")
    public Lecon findLeconById(@PathVariable("id") Integer id);


    @DeleteMapping(LECON_ENDPOINT + "/delete/{id}")
    public void deleteLecon( @PathVariable("id") Integer id);

    @PutMapping(LECON_ENDPOINT + "/update")
    public Lecon UpdateLecon(@RequestBody Lecon lecon, @RequestHeader(name = "Authorization") String jwt);

}
