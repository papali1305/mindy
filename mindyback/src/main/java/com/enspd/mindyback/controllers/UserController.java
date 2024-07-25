package com.enspd.mindyback.controllers;

import com.enspd.mindyback.controllers.api.UserApi;
import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.exception.UtilisateurException;
import com.enspd.mindyback.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static com.enspd.mindyback.config.Utils.USER_ENDPOINT;

@RestController(USER_ENDPOINT)
public class UserController implements UserApi {

    @Autowired
    private UserService userService;

    @Override
    public UserDto findUser(Integer id) throws UtilisateurException {
        return userService.findUserById(id);
    }
}
