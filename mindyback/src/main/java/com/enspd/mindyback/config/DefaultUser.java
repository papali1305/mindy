package com.enspd.mindyback.config;

import com.enspd.mindyback.models.User;
import com.enspd.mindyback.models.type.AccountStatus;
import com.enspd.mindyback.models.type.Gender;
import com.enspd.mindyback.models.type.SeverityLevel;
import com.enspd.mindyback.services.UserService;
import com.enspd.mindyback.services.impl.UserServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component

public class DefaultUser {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostConstruct
    public void init() {
        try {
            /*Create new default user*/
            User user = new User();
            user.setMail("name@gmail.com");
            user.setPassword(passwordEncoder.encode("password"));
            user.setFirstName("Lontsi");
            user.setLastName("Hermann");
            user.setAge(25);
            user.setGender(Gender.MALE);
            user.setComorbidities("asthme");
            user.setSeverityLevel(SeverityLevel.MILD);
            user.setAccountStatus(AccountStatus.ACTIVE);
            user.setCity("Douala");
            user.setState("Cameroon");
            user.setCountry("Cameroon");
            userService.createUtilisateur(user);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
