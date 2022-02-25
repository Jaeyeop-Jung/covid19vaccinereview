package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
import com.teamproject.covid19vaccinereview.dto.CommentWriteRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class CommentRestAssuredCRUD {

    public static ExtractableResponse<Response> postComment(String accessToken, String postId, CommentWriteRequest commentWriteRequest){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", accessToken)
                .body(commentWriteRequest)
                .when()
                .post("/post/" + postId + "/comment")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getComment(String accessToken, String postId){
        return RestAssured
                .given().log().all()
                .header("Authorization", accessToken)
                .when()
                .get("/post/" + postId + "/comment")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> patchComment(String accessToken, String postId, String commentId, String content){
        return RestAssured
                .given().log().all()
                .contentType("application/json; charset=utf-8")
                .header("Authorization", accessToken)
                .body(content)
                .when()
                .patch("/post/" + postId + "/comment/" + commentId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteComment(String accessToken, String postId, String commentId){
        return RestAssured
                .given().log().all()
                .header("Authorization", accessToken)
                .when()
                .delete("/post/" + postId + "/comment/" + commentId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> patchCommentLike(String accessToken, String postId, String commentId){
        return RestAssured
                .given().log().all()
                .header("Authorization", accessToken)
                .when()
                .patch("/post/" + postId + "/comment/" + commentId +"/like")
                .then().log().all()
                .extract();
    }
}
