package com.example.demo.services;

import com.example.demo.models.ErrorMessage;
import com.example.demo.models.Vacuum;
import com.example.demo.repositories.ErrorMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ErrorMessageService {

    @Autowired
    private ErrorMessageRepository errorMessageRepository;

    public ErrorMessage createError(Vacuum vacuum, String operation, String msg, Date schedTime, String email) {
        ErrorMessage em = new ErrorMessage();
        em.setVacuum(vacuum);
        em.setOperation(operation);
        em.setErrorMessage(msg);
        em.setScheduledTime(schedTime);
        em.setUser(email);

        this.errorMessageRepository.save(em);

        return em;
    }

    public List<ErrorMessage> getErrosForUser(String email) {
        return this.errorMessageRepository.findAllByUser(email);
    }
}
