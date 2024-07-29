package com.enspd.mindyback.models;

import com.enspd.mindyback.models.type.AccountStatus;
import com.enspd.mindyback.models.type.Gender;
import com.enspd.mindyback.models.type.SeverityLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Table
@AllArgsConstructor
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity {

    @Column
    private String lastName;

    @Column
    private String firstName;

    @Column
    private Integer age;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String password;

    @Column
    private String photo;


    @Column
    @Enumerated(EnumType.STRING)
    private SeverityLevel severityLevel;

    @Column
    private String comorbidities;

    @Column
    private String mail;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String postal_code;

    @Column
    private String country;

    @Column
    private Integer phoneNumber;

    @Column(columnDefinition = " VARCHAR(255)  DEFAULT 'ACTIVE' ")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "rewardId")
    private Reward reward;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Competence> competences;
}
