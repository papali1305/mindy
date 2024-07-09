package com.enspd.mindyback.services;

import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.models.Communication;

import java.util.List;

public interface CommunicationService {
    public List<Communication> createCommunications(Lecon lecon);

    Communication findCommunication(Integer idCommunication);

    void deleteCommunication(Integer idCommunication);

    Communication updateCommunication(Communication Communication);
}
