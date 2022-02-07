package com.teamproject.covid19vaccinereview.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.covid19vaccinereview.domain.Board;
import com.teamproject.covid19vaccinereview.domain.VaccineType;
import com.teamproject.covid19vaccinereview.dto.PostWriteRequest;
import com.teamproject.covid19vaccinereview.repository.BoardRepository;
import com.teamproject.covid19vaccinereview.utils.CreatePostRequestUtil;
import com.teamproject.covid19vaccinereview.utils.PostRestAssuredCRUD;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("UserApiController 테스트")
public class PostApiControllerTest {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final CreatePostRequestUtil createPostRequestUtil;
    private final BoardRepository boardRepository;

    @Autowired
    public PostApiControllerTest(ObjectMapper objectMapper, ResourceLoader resourceLoader, CreatePostRequestUtil createPostRequestUtil, BoardRepository boardRepository) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.createPostRequestUtil = createPostRequestUtil;
        this.boardRepository = boardRepository;
    }

    @LocalServerPort
    int port;

    @AfterEach
    void afterEach(){
    }

    @Test
    @DisplayName("이미지가 없는 게시글 작성 테스트")
    public void 이미지가_없는_게시글_작성_을_테스트한다(){
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();
        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
        int randomOrdinalNumber = (int)( Math.random() * 100 );
        Board randomBoard = boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));

        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);

        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(objectMapper.convertValue(postWriteRequestWithUUID, Map.class));

        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        //post 테스트 작성하기
    }

}
