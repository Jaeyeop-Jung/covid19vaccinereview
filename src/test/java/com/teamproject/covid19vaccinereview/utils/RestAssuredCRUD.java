package com.teamproject.covid19vaccinereview.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.Map;

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

    public static ExtractableResponse<Response> postOriginUser(Map joinRequest){
        return RestAssured
                .given().log().all()
                .queryParams(joinRequest)
                .when()
                .post("/user")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postOriginUserWithProfileImage(Map joinRequest, File file){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("multipartFile", file, MediaType.MULTIPART_FORM_DATA_VALUE)
                .queryParams(joinRequest)
                .when()
                .post("/user")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getOriginLogin(Map loginRequest){
        return  RestAssured
                .given().log().all()
                .queryParams(loginRequest)
                .when()
                .get("/login")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getWithAccessToken(String path, String accestoken){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", accestoken)
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putWithUserInfo(String accessToken, String password, String nickname, File file, boolean changeProfileImage){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header("Authorization", accessToken)
                .param("password", password)
                .param("nickname", nickname)
                .param("changeProfileImage", changeProfileImage)
                .multiPart("multipartFile", file, MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .put("/user")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteWithAccessToken(String accessToken){
        return RestAssured
                .given().log().all()
                .header("Authorization", accessToken)
                .when()
                .delete("/user")
                .then().log().all()
                .extract();
    }

}
