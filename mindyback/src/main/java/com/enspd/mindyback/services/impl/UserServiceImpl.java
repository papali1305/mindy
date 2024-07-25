package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.config.JwtProvider;
import com.enspd.mindyback.dto.CompetenceDto;
import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.exception.EntityNotFoundException;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.exception.InvalidOperationException;
import com.enspd.mindyback.exception.UtilisateurException;
import com.enspd.mindyback.models.Competence;
import com.enspd.mindyback.models.Reward;
import com.enspd.mindyback.models.User;
import com.enspd.mindyback.models.type.AccountStatus;
import com.enspd.mindyback.models.type.CompetenceType;
import com.enspd.mindyback.repository.UserRepository;
import com.enspd.mindyback.services.CompetenceService;
import com.enspd.mindyback.services.RewardService;
import com.enspd.mindyback.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompetenceService   competenceService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RewardService rewardService;

    @Override
    public UserDto findUserById(Integer userId) throws UtilisateurException {
        User utilisateur = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("aucun utilisateur trouve avec l id:" + userId, ErrorCodes.USER_NOT_FOUND));

        return UserDto.fromEntity(utilisateur);
    }

    @Override
    public UserDto findUserByJwt(String jwt)  {
        String email = jwtProvider.getEmailFromToken(jwt);
        User utilisateur = userRepository.findByMail(email);
        if (utilisateur != null) {
            return UserDto.fromEntity(utilisateur);
        }
        throw new EntityNotFoundException("Aucun utilisateur avec l email fournie par le token :" + jwt, ErrorCodes.USER_NOT_FOUND);
    }

    @Override
    @Transactional
    public UserDto createUtilisateur(User utilisateur)   {

        User test = userRepository.findByMail(utilisateur.getMail());
        if (utilisateur.getMail() == null) {
            throw new InvalidOperationException("Email is null", ErrorCodes.USER_NOT_VALID);
        }
        if (test != null) {
            throw new InvalidOperationException("Un utilisateur existe deja avec cet email " + utilisateur.getMail() , ErrorCodes.USER_ALREADY_EXIST);
        }

        if (utilisateur.getAccountStatus() == null) {
            utilisateur.setAccountStatus(AccountStatus.ACTIVE);
        }

        Reward reward = new Reward();
        utilisateur.setReward(reward);
        List<Competence> competences = new ArrayList<>();

        Competence competence1 = new Competence(CompetenceType.SOCIAL);
        Competence competence2 = new Competence(CompetenceType.NO_VERBAL_CONV);
        Competence competence3 = new Competence(CompetenceType.VERBAL_CONV);

        competences.add(competence1);
        competences.add(competence2);
        competences.add(competence3);



        User user = userRepository.save(utilisateur);

        for (Competence competence : competences) {
            competence.setUser(user);
            competenceService.createCompetence(CompetenceDto.fromEntity(competence));
        }

        return UserDto.fromEntity(user);
    }


    @Override
    public User updateUtilisateur(User newUser) throws UtilisateurException, NoSuchMethodException {

        try {
            User lastUser = findUserByEmail(newUser.getMail());

            List<String> fields = List.of("Photo", "FirstName", "Password", "Gender", "Age", "Mail", "LastName",
                    "City", "Country", "AccountStatus", "State", "Postal_code",
                    "Comorbidities", "SeverityLevel", "PhoneNumber");
            for (String field : fields) {
                Method getter = User.class.getMethod("get" + field);
                Method setter = User.class.getMethod("set" + field);

                Object newValue = getter.invoke(newUser);
                Object oldValue = getter.invoke(lastUser);

                if (newValue != null && !Objects.equals(newValue, oldValue)) {
                    setter.invoke(lastUser, newValue);
                }
            }
            return userRepository.save(lastUser);
        } catch (UtilisateurException exception) {
            throw new UtilisateurException("impossible de mettre a jour les info de cet utilisateur il n existe pas d utilisateur avec le mail:" + newUser.getMail());
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException("impossible d executer l update, la methode n existe pas");
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public User findUserByEmail(String email) throws UtilisateurException {
        User utilisateur = userRepository.findByMail(email);
        if (utilisateur != null) {

            return utilisateur;
        }
        throw new UtilisateurException("Aucun utilisateur avec l email :" + email);


    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) throws UtilisateurException {
        User utilisateur = userRepository.findById(userId).orElseThrow(() -> new InvalidOperationException("Impossible de supprimer l utilisateur aucun utilisateur trouve avec l id:" + userId, ErrorCodes.USER_NOT_FOUND));
        userRepository.deleteById(userId);
    }

}
