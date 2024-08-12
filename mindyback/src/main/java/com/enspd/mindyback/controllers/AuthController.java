package com.enspd.mindyback.controllers;

import com.enspd.mindyback.config.JwtProvider;
import com.enspd.mindyback.dto.AuthDto;
import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.exception.EntityNotFoundException;
import com.enspd.mindyback.exception.ErrorCodes;
import com.enspd.mindyback.models.User;
import com.enspd.mindyback.models.type.Gender;
import com.enspd.mindyback.models.type.SeverityLevel;
import com.enspd.mindyback.response.AuthResponse;
import com.enspd.mindyback.services.UserService;
import com.enspd.mindyback.services.impl.UtilisateurDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.enspd.mindyback.config.Utils.AUTHENTICATION_ENDPOINT;


@RestController
@RequestMapping(AUTHENTICATION_ENDPOINT)
@Schema(description = "Création et connexion d'un utilisateur retourne un token JWT")
public class AuthController {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;


    @Autowired
    private UtilisateurDetailsServiceImpl customUserDetails;


    @PostMapping("/signup")
    @Operation(summary = "Création d'un utilisateur." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Création d'un utilisateur", content = @Content(schema = @Schema(implementation =  AuthResponse.class ))),
            @ApiResponse(responseCode = "400", description = "Mauvais format de données"),
    })
    public ResponseEntity<AuthResponse> createUtilisateurHandler(@RequestBody UserDto user) {

        String firstName = user.firstName();
       // String lastName = user.lastName();
        String mail = user.mail();
        String password = user.password();
        Gender gender = Gender.valueOf(String.valueOf(user.gender()).toUpperCase());
        Integer age = user.age();
     //   String city = user.city();
     //   String state = user.state();
    //    String country = user.country();
        String comorbidities = user.comorbidities();
        SeverityLevel severityLevel = SeverityLevel.valueOf(String.valueOf(user.severityLevel()).toUpperCase());
    //    Integer phoneNumber = user.phoneNumber();

        User createdUSer = new User();

        createdUSer.setFirstName(firstName);
        createdUSer.setMail(mail);
        createdUSer.setPassword(passwordEncoder.encode(password));
      //  createdUSer.setLastName(lastName);
        createdUSer.setGender(gender);
        createdUSer.setAge(age);
     //   createdUSer.setCity(city);
     //   createdUSer.setState(state);
     //   createdUSer.setCountry(country);
        createdUSer.setComorbidities(comorbidities);
        createdUSer.setSeverityLevel(severityLevel);
      //  createdUSer.setPhoneNumber(phoneNumber);


        System.out.println(createdUSer);

        userService.createUtilisateur(createdUSer);


        Authentication authentication = new UsernamePasswordAuthenticationToken(mail, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/signin")
    @Operation(summary = "Connexion d'un utilisateur" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Création d'un utilisateur", content = @Content(schema = @Schema(implementation =  AuthResponse.class ))),
            @ApiResponse(responseCode = "1000", description = "Mauvais format de données"),
    })
    public ResponseEntity<AuthResponse> signin(@RequestBody AuthDto authDto) {

        System.out.println("-------------------------------");
        String password = authDto.getPassword();
        String email = authDto.getMail();

        Authentication authentication = authenticate(email, password);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse(token, true);
        return new ResponseEntity<AuthResponse>(res, HttpStatus.ACCEPTED);

    }

    private Authentication authenticate(String email, String password) {
        try {
            UserDetails userDetails = customUserDetails.loadUserByUsername(email);

            if (userDetails == null) {
                throw new BadCredentialsException("invalid email...");
            }
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("invalid email or password");
            }
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        } catch (UsernameNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage(), ErrorCodes.USER_NOT_FOUND);
        }

    }
}
