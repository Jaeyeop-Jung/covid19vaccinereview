package com.teamproject.covid19vaccinereview.domain;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String content;

    private String url;

    @Column(name = "WATCHED")
    private boolean watched;

    @NotNull
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "DATE_WATCHED")
    private LocalDateTime dateWatched;

}
