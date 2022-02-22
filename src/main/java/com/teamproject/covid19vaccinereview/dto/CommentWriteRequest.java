package com.teamproject.covid19vaccinereview.dto;

import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentWriteRequest {

    @NotBlank
    @NotNull
    @ApiParam(required = true)
    private String content;

    private long parentId;

}
