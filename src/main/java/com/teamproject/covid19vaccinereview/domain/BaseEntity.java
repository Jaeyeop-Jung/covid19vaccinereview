package com.teamproject.covid19vaccinereview.domain;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @NotNull
    private LocalDateTime dateCreated;

    private LocalDateTime lastUpdated;


    
}
