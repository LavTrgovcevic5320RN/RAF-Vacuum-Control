package com.example.demo.services;

import com.example.demo.models.Task;
import com.example.demo.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Service
public class VacuumScheduleService {

    @Autowired
    private VacuumService vacuumService;

    @Autowired
    private TaskRepository taskRepository;

    public void scheduleTask(String email, Long vacuumId, String action, LocalDateTime shedTime) {
        Date triggerTime = Date.from(shedTime.atZone(ZoneId.systemDefault()).toInstant());
        Task task = new Task();

        task.setVacuumId(vacuumId);
        task.setDone(false);
        task.setSchedTime(triggerTime);
        task.setEmail(email);
        task.setAction(action);

        taskRepository.save(task);
    }

    public void checkTasks() {
        try {
            Date triggerTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            ArrayList<Task> tasks = new ArrayList<>(this.taskRepository.findUnexecutedTasks(triggerTime));

            if(tasks.size() == 0) {
                return;
            }

            executeTasks(tasks);

            System.out.println(tasks.get(0).getVacuumId());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeTasks(ArrayList<Task> tasks) {
        for(Task t: tasks) {
            switch(t.getAction()) {
                case "START":
                    this.vacuumService.startVacuum(t.getEmail(),t.getVacuumId());
                    System.out.println("Executing START: " + t.getVacuumId());
                    break;
                case "STOP":
                    this.vacuumService.stopVacuum(t.getEmail(),t.getVacuumId());
                    System.out.println("Executing STOP: " + t.getVacuumId());
                    break;
                case "DISCHARGE":
                    this.vacuumService.dischargeVacuum(t.getEmail(),t.getVacuumId());
                    System.out.println("Executing DISCHARGE: " + t.getVacuumId());
                    break;
            }

            t.setDone(true);
            this.taskRepository.save(t);
        }
    }
}
