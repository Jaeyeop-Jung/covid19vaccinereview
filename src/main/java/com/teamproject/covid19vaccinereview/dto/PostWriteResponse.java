package com.teamproject.covid19vaccinereview.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PostWriteResponse {

    private Long id;

    private String location;

    @Builder
    public PostWriteResponse(Long id, String location) {
        this.id = id;
        this.location = location;
    }
}
