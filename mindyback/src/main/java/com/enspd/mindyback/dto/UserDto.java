package com.enspd.mindyback.dto;

import com.enspd.mindyback.models.User;
import com.enspd.mindyback.models.type.AccountStatus;
import com.enspd.mindyback.models.type.Gender;
import com.enspd.mindyback.models.type.SeverityLevel;

import java.time.Instant;

public record UserDto(Integer id, Instant creationDate, Instant lastModifiedDate, String firstName, String lastName,
                      String mail, Integer age, Gender gender,
                      String password, String photo, SeverityLevel severityLevel, String comorbidities,
                      String city, String state, String postal_code, String country, Integer phoneNumber,
                      AccountStatus accountStatus) {


    public static UserDto fromEntity(User user) {

        return new UserDto(user.getId(), user.getCreationDate(), user.getLastModifiedDate(), user.getFirstName(), user.getLastName(), user.getMail(),
                user.getAge(), user.getGender(), user.getPassword(), user.getPhoto(), user.getSeverityLevel(),
                user.getComorbidities(), user.getCity(), user.getState(), user.getPostal_code(),
                user.getCountry(), user.getPhoneNumber(), user.getAccountStatus());
    }


    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.id());
        user.setCreationDate(dto.creationDate());
        user.setLastModifiedDate(dto.lastModifiedDate());
        user.setAge(dto.age());
        user.setMail(dto.mail());
        user.setPassword(dto.password());
        user.setGender(dto.gender());
        user.setLastName(dto.lastName());
        user.setFirstName(dto.firstName());
        user.setPhoto(dto.photo());
        user.setSeverityLevel(dto.severityLevel());
        user.setComorbidities(dto.comorbidities());
        user.setCity(dto.city());
        user.setState(dto.state());
        user.setPostal_code(dto.postal_code());
        user.setCountry(dto.country());
        user.setPhoneNumber(dto.phoneNumber());
        user.setAccountStatus(dto.accountStatus());
        return user;
    }
}
