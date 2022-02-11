package com.teamproject.covid19vaccinereview.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.List;
import java.util.Map;

public class PostRestAssuredCRUD {

    public static ExtractableResponse<Response> postPostWrite(String accessToken, Map postWriteRequest){
        return RestAssured
                .given().log().all()
                .header("Authorization", accessToken)
                .queryParams(postWriteRequest)
                .when()
                .post("/post")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postPostWriteWithPostImage(String accessToken, Map postWriteRequest, List<File> fileList) {

        RequestSpecification reqeust =
                RestAssured.given().log().all()
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header("Authorization", accessToken)
                        .queryParams(postWriteRequest);
        for (File file : fileList) {
            reqeust.multiPart("multipartFileList", file, MediaType.MULTIPART_FORM_DATA_VALUE);
        }

        return reqeust.when()
                .post("/post")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getPostById(long id){
        return RestAssured
                .given().log().all()
                .when()
                .get("/post/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putPostById(long id, String accessToken, Map modifyPostRequest, List<File> fileList){
        RequestSpecification reqeust =
                RestAssured.given().log().all()
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header("Authorization", accessToken)
                        .queryParams(modifyPostRequest);
        for (File file : fileList) {
            reqeust.multiPart("multipartFileList", file, MediaType.MULTIPART_FORM_DATA_VALUE);
        }

        return reqeust.when()
                .post("/post/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putTitleOrContentPostById(long id, String accessToken, Map modifyPostRequest, List<File> fileList){
        if(fileList == null){
            return  RestAssured.given().log().all()
                            .header("Authorization", accessToken)
                            .queryParams(modifyPostRequest)
                            .when()
                            .put("/post/" + id)
                            .then().log().all()
                            .extract();
        }

        RequestSpecification reqeust =
                RestAssured.given().log().all()
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header("Authorization", accessToken)
                        .queryParams(modifyPostRequest);
        for (File file : fileList) {
            reqeust.multiPart("multipartFileList", file, MediaType.MULTIPART_FORM_DATA_VALUE);
        }
        return reqeust.when()
                .put("/post/" + id)
                .then().log().all()
                .extract();
    }

}
