package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import javax.print.attribute.standard.Media;
import java.io.File;

public class RestAssuredCRUD {

    public static ExtractableResponse<Response> postRequest(String path, Object request) {
        return RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> postRequestWithOAuth(String path, Object request, String token) {
        return RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .when()
            .post(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> get(String path) {
        return RestAssured.given().log().all()
            .when()
            .get(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getWithOAuth(String path, String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> putRequest(String path, Object request) {
        return RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> putRequestWithAOuth(String path, Object request, String token) {
        return RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .when()
            .put(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete(String path) {
        return RestAssured.given().log().all()
            .when()
            .delete(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> deleteWithOAuth(String path, String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .delete(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> postOriginJoinRequest(String joinRequest){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("joinRequest", joinRequest, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/join")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postOriginJoinRequestWithProfileImage(String joinRequest, File file){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("multipartFile", file, MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("joinRequest", joinRequest, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/join")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postOriginLogin(String loginRequest){
        return  RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .when()
                .post("/login")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getRequestWithAccessToken(String path, String accestoken){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", accestoken)
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

}
