package com.teamproject.covid19vaccinereview.controller;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("UserApiController 테스트")
public class IndexControllerTest {

    @LocalServerPort
    int port;

    @DisplayName("Index 페이지 반환 테스트")
    @Test
    void 인덱스_페이지_반환_테스트(){
        RestAssured.port = port;

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }


}
