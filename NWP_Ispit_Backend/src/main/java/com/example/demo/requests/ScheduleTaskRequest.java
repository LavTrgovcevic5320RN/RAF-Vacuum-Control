package com.example.demo.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleTaskRequest {
    private Long vacuumId;
    private String action;
    private LocalDateTime schedTime;
}
