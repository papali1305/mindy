package com.enspd.mindyback;

import com.enspd.mindyback.models.User;
import com.enspd.mindyback.models.type.AccountStatus;
import com.enspd.mindyback.models.type.Gender;
import com.enspd.mindyback.models.type.SeverityLevel;
import com.enspd.mindyback.services.UserService;
import com.enspd.mindyback.services.impl.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.json.JSONObject;

@SpringBootApplication

@EnableJpaAuditing
@EnableScheduling
public class MindybackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindybackApplication.class, args);

    }
}

