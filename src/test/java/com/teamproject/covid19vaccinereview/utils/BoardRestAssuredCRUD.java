package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class BoardRestAssuredCRUD {

    public static ExtractableResponse<Response> postBoard(VaccineType vaccineType, int ordinalNumber){
        return RestAssured
                .given().log().all()
                .queryParam("vaccineType", vaccineType)
                .queryParam("ordinalNumber", ordinalNumber)
                .when()
                .post("/board")
                .then()
                .log().all()
                .extract();
    }
}
