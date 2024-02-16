package com.example.demo.controllers;

import com.example.demo.models.Vacuum;
import com.example.demo.requests.VacuumRequest;
import com.example.demo.responses.OperationMessage;
import com.example.demo.services.VacuumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/vacuums")
public class VacuumRestController {
    @Autowired
    private VacuumService vacuumService;

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('can_search_vacuums')")
    public ResponseEntity<List<Vacuum>> searchVacuums(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> status,
            @RequestParam(required = false) Date dateFrom,
            @RequestParam(required = false) Date dateTo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try{
            List<Vacuum> vacs = vacuumService.searchVacuums(name, status, dateFrom, dateTo, auth.getName());

            return ResponseEntity.ok(vacs);
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }

    @PostMapping("/discharge/{vacuumId}")
    @PreAuthorize("hasAuthority('can_discharge_vacuums')")
    public ResponseEntity<?> dischargeVacuum(@PathVariable Long vacuumId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            vacuumService.dischargeVacuum(auth.getName(), vacuumId);

            return ResponseEntity.ok(new OperationMessage("Vacuum discharged started!"));
        }catch(Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong when adding the vacuum");
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('can_add_vacuums')")
    public ResponseEntity<?> addVacuum(@RequestBody VacuumRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            Vacuum vacuum = vacuumService.addVacuum(req, auth.getName());

            return ResponseEntity.ok(vacuum);
        }catch(Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong when adding the vacuum");
        }
    }

    @PostMapping("/start/{vacuumId}")
    @PreAuthorize("hasAuthority('can_start_vacuums')")
    public ResponseEntity<?> startVacuum(@PathVariable Long vacuumId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            vacuumService.startVacuum(auth.getName(), vacuumId);

            return ResponseEntity.ok(new OperationMessage("Vacuum process started"));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong when starting vacuum " + vacuumId);
        }
    }

    @PostMapping("/stop/{vacuumId}")
    @PreAuthorize("hasAuthority('can_stop_vacuums')")
    public ResponseEntity<?> stopVacuum(@PathVariable Long vacuumId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            vacuumService.stopVacuum(auth.getName(), vacuumId);

            return ResponseEntity.ok(new OperationMessage("Vacuum STOP process started"));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong when starting vacuum " + vacuumId);
        }
    }

    @PostMapping("/remove/{vacuumId}")
    @PreAuthorize("hasAuthority('can_remove_vacuums')")
    public ResponseEntity<?> removeVacuum(@PathVariable Long vacuumId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            boolean flag = vacuumService.removeVacuum(auth.getName(),vacuumId);

            if(flag) {
                return ResponseEntity.ok(true);
            }

            return ResponseEntity.badRequest().body(false);
        }catch(Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong removing vacuum " + vacuumId);
        }
    }
}
