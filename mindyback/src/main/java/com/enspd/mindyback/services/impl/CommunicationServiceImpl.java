package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.dto.UserDto;
import com.enspd.mindyback.models.Communication;
import com.enspd.mindyback.models.Lecon;
import com.enspd.mindyback.repository.CommunicationRepository;
import com.enspd.mindyback.services.CommunicationService;
import com.enspd.mindyback.services.IaService;
import com.enspd.mindyback.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService {

    @Autowired
    private IaService iaService;
    @Autowired
    private CommunicationRepository communicationRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional

    public List<Communication> createCommunications(Lecon lecon, String jwt) {
        UserDto userDto = userService.findUserByJwt(jwt);

        List<Communication> communications = iaService.createCommunication(lecon, userDto);
        List<Communication> communicationsToSend = new ArrayList<>();
        communications.forEach(communication -> {
            communication.setPassed(false);
            communication.setLecon(lecon);
            communicationsToSend.add(communicationRepository.save(communication));

        });
        return communicationsToSend;
    }

    @Override
    public Communication findCommunication(Integer idCommunication) {
        return communicationRepository.findById(idCommunication).orElseThrow(() -> new RuntimeException("Impossible de trouver la communication avec l id " + idCommunication));
    }

    @Override
    public void deleteCommunication(Integer idCommunication) {
        communicationRepository.deleteById(idCommunication);
    }

    @Override
    public Communication updateCommunication(Communication Communication) {

        Communication existingCommunication = findCommunication(Communication.getId());
        BeanUtils.copyProperties(Communication, existingCommunication, "id" );
        return communicationRepository.save(existingCommunication);
    }
}
