package com.enspd.mindyback.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Communication extends AbstractEntity {

    @Column
    private String aiConv;

    @Column
    private String contexte;


    @Column
    @Enumerated(EnumType.STRING)
    private CommunicationType communicationType;

    @Column
    private boolean isPassed;

    @Column
    private String userResponse;

    @Column
    private String correction;

    @ManyToOne
    @JoinColumn(name = "id_lecon")
    private Lecon lecon;
}
