package com.teamproject.covid19vaccinereview.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.Map;

public class PostRestAssuredCRUD {

    public static ExtractableResponse<Response> postPostWrite(Map postWriteRequest){
        return RestAssured
                .given().log().all()
                .queryParams(postWriteRequest)
                .when()
                .post("/post")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postPostWriteWithPostImage(Map postWriteRequest, File[] file){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("multipartFile", file, MediaType.MULTIPART_FORM_DATA_VALUE)
                .queryParams(postWriteRequest)
                .when()
                .post("/user")
                .then()
                .log().all()
                .extract();
    }
}
