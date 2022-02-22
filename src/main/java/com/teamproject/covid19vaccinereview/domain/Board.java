package com.teamproject.covid19vaccinereview.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "BOARDS")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARDS_ID")
    private Long id;

    @Column(name = "VACCINE_TYPES")
    @Enumerated(EnumType.STRING)
    private VaccineType vaccineType;

    @Column(name = "ORDINAL_NUMBER")
    private int ordinalNumber;

    private Board(VaccineType vaccineType, int ordinalNumber) {
        this.vaccineType = vaccineType;
        this.ordinalNumber = ordinalNumber;
    }

    public static Board of(VaccineType vaccineType, int ordinalNumber){
        return new Board(vaccineType, ordinalNumber);
    }

}
