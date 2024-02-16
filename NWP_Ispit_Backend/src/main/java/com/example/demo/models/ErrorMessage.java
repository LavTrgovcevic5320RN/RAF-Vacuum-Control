package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "error_messages")
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String user;

    @ManyToOne
    @JoinColumn(name = "vacuum_id")
    private Vacuum vacuum;

    @Column(name = "operation", nullable = false)
    private String operation;

    @Column(name = "error_message", nullable = false)
    private String errorMessage;

    @Column(name = "scheduled_time", nullable = false)
    private Date scheduledTime;
}
