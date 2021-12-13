package com.mikehenry.springbootslice.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskID")
    private Long taskID;

    @Column(name = "taskName")
    private String taskName;

    @Column(name = "description")
    private String description;

    @Column(name = "assigner")
    private String assigner;

    @Column(name = "assignee")
    private String assignee;
}
