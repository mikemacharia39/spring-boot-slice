package com.mikehenry.springbootslice.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    // @GeneratedValue(strategy = GenerationType.IDENTITY) -- use this to autoincrement
    // this one uses a table to generate ids
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1)
    @Column(name = "employeeID")
    private Long employeeID;

    @Column(nullable = false)
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "emailAddress")
    private String emailAddress;

    private int active;

    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModified;

    private String changeDetails;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    private List<Task> tasks;
}
