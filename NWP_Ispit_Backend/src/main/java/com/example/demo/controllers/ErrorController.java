package com.example.demo.controllers;

import com.example.demo.models.ErrorMessage;
import com.example.demo.services.ErrorMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/errors")
public class ErrorController {

    @Autowired
    private ErrorMessageService errorMessageService;

    @GetMapping
    public ResponseEntity<?> getUsersErrors() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            List<ErrorMessage> errors = this.errorMessageService.getErrosForUser(auth.getName());

            return ResponseEntity.ok(errors);
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

}
