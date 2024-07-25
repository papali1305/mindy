package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.exception.UtilisateurException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.enspd.mindyback.config.Utils.USER_ENDPOINT;

public interface UserApi {

    @GetMapping(USER_ENDPOINT + "/find/{id}")
    public UserDto findUser(@PathVariable("id") Integer id) throws UtilisateurException;

}
