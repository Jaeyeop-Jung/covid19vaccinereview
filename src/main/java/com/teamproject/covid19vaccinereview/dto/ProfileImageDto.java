package com.teamproject.covid19vaccinereview.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImageDto {

    private String data;

    private String name;

    private String size;

    private String contentType;
}
