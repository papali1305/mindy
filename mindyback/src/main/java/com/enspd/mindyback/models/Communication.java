package com.enspd.mindyback.models;

import com.enspd.mindyback.models.type.CommunicationType;
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
public class Communication extends Game {

    @Column(columnDefinition = "TEXT", length = 2000)
    private String aiConv;

    @Column
    private String context;


    @Column
    @Enumerated(EnumType.STRING)
    private CommunicationType communicationType;


}
