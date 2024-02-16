package com.example.demo.controllers;

import com.example.demo.repositories.TaskRepository;
import com.example.demo.requests.ScheduleTaskRequest;
import com.example.demo.responses.OperationMessage;
import com.example.demo.services.VacuumScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/vacuum-schedule")
public class ScheduleRestController {

    @Autowired
    private VacuumScheduleService vacuumScheduleService;

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<?> scheduleTask(@RequestBody ScheduleTaskRequest shedReq) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(shedReq);
        vacuumScheduleService.scheduleTask(auth.getName(), shedReq.getVacuumId(), shedReq.getAction(), shedReq.getSchedTime());
        return ResponseEntity.ok(new OperationMessage("Successfully sheduled " + shedReq.getAction()));
    }

    @Scheduled(fixedDelay = 1000)
    public void checkTasks() {
        this.vacuumScheduleService.checkTasks();
        System.out.println("Tasks checked");
    }
}
