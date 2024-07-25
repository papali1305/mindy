package com.enspd.mindyback.services;

import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.exception.UtilisateurException;
import com.enspd.mindyback.models.User;


public interface UserService {


    public UserDto findUserById(Integer userId) throws UtilisateurException;

    public UserDto findUserByJwt(String jwt);

    public UserDto createUtilisateur (User utilisateur) ;

    public  User updateUtilisateur(User utilisateur) throws UtilisateurException, NoSuchMethodException;
    public  User findUserByEmail(String email) throws UtilisateurException;

    public  void deleteUser(Integer userId) throws UtilisateurException;

}
