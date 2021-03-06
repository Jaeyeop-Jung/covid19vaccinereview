package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.dto.PostSearchType;
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

    public static ExtractableResponse<Response> getAllPost(String accessToken){

        RequestSpecification request = RestAssured
                .given().log().all();
        if(accessToken != null){
            request.header("Authorization", accessToken);
        }
        return request
                .when()
                .get("/post")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getPostById(String accessToken, long id){

        RequestSpecification request = RestAssured
                .given().log().all();
        if(accessToken != null){
            request.header("Authorization", accessToken);
        }
        return request
                .get("/post/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getSearchPost(String accessToken, PostSearchType postSearchType, String keyword, int page){

        RequestSpecification request = RestAssured
                .given().log().all()
                .queryParam("postSearchType", postSearchType)
                .queryParam("keyword", keyword)
                .queryParam("page", page);
        if(accessToken != null){
            request.header("Authorization", accessToken);
        }
        return request
                .when()
                .get("/post/search")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> patchPostById(long id, String accessToken, Map modifyPostRequest, List<File> fileList){

        if(fileList == null){

            return RestAssured.given().log().all()
                    .header("Authorization", accessToken)
                    .queryParams(modifyPostRequest)
                    .when()
                    .patch("/post/" + id)
                    .then().log().all()
                    .extract();

        } else{
            RequestSpecification request = RestAssured.given().log().all()
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .header("Authorization", accessToken)
                    .params(modifyPostRequest);
            for (File file : fileList) {
                request.multiPart("multipartFileList", file, MediaType.MULTIPART_FORM_DATA_VALUE);
            }
            return request.when()
                    .patch("/post/" + id)
                    .then().log().all()
                    .extract();
        }
    }

    public static ExtractableResponse<Response> patchTitleOrContentPostById(long id, String accessToken, Map modifyPostRequest, List<File> fileList){
        if(fileList == null){
            return  RestAssured.given().log().all()
                            .header("Authorization", accessToken)
                            .queryParams(modifyPostRequest)
                            .when()
                            .patch("/post/" + id)
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
                .patch("/post/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deletePostById(String accessToken, long id){
        return RestAssured.given().log().all()
                .header("Authorization", accessToken)
                .when()
                .delete("/post/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> patchLkePostById(String accessToken, long id){
        return RestAssured.given().log().all()
                .header("Authorization", accessToken)
                .when()
                .patch("/post/" + id + "/like")
                .then().log().all()
                .extract();
    }
}
