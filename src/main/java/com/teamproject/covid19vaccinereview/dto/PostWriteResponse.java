package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PostWriteResponse {

    private Long id;

    private String location;

    private LocalDateTime dateCreated;

    @Builder
    public PostWriteResponse(Long id, String location, LocalDateTime dateCreated) {
        this.id = id;
        this.location = location;
        this.dateCreated = dateCreated;
    }
}
