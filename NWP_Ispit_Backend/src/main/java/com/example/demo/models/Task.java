package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long vacuumId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private boolean isDone;

    @Temporal(TemporalType.TIMESTAMP)
    private Date schedTime;
}
