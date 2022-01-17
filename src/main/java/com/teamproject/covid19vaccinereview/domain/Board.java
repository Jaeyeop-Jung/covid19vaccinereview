package com.teamproject.covid19vaccinereview.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "BOARDS")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARDS_ID")
    private Long id;

    @Column(name = "TYPES")
    private String Type;

    @Column(name = "ORDINAL_NUMBER")
    private int ordinalNumber;

}