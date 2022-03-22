package com.mikehenry.springbootslice.model;

import com.mikehenry.springbootslice.repository.AddressPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @EmbeddedId
    private AddressPK id;

    @Column(name = "poBox")
    private String poBox;
}
