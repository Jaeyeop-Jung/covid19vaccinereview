package com.teamproject.covid19vaccinereview.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.covid19vaccinereview.domain.Board;
import com.teamproject.covid19vaccinereview.domain.VaccineType;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.ModifyPostRequest;
import com.teamproject.covid19vaccinereview.dto.PostWriteRequest;
import com.teamproject.covid19vaccinereview.repository.BoardRepository;
import com.teamproject.covid19vaccinereview.repository.PostImageRepository;
import com.teamproject.covid19vaccinereview.repository.PostRepository;
import com.teamproject.covid19vaccinereview.utils.*;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("PostApiController 테스트")
public class PostApiControllerTest {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final CreateUserRequestUtil createUserRequestUtil;
    private final CreatePostRequestUtil createPostRequestUtil;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final JsonParseUtil jsonParseUtil;

    @Autowired
    public PostApiControllerTest(ObjectMapper objectMapper, ResourceLoader resourceLoader, CreatePostRequestUtil createPostRequestUtil, BoardRepository boardRepository, PostRepository postRepository, CreateUserRequestUtil createUserRequestUtil, PostImageRepository postImageRepository, JsonParseUtil jsonParseUtil) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.createPostRequestUtil = createPostRequestUtil;
        this.boardRepository = boardRepository;
        this.postRepository = postRepository;
        this.createUserRequestUtil = createUserRequestUtil;
        this.postImageRepository = postImageRepository;
        this.jsonParseUtil = jsonParseUtil;
    }

    @LocalServerPort
    int port;

    @AfterEach
    void afterEach(){
        boardRepository.deleteAll();
        postRepository.deleteAll();
        postImageRepository.deleteAll();
    }

    @Test
    @DisplayName("잘못된 토큰으로 게시글 작성")
    public void 잘못된_토큰_을_이용하여_게시글_작성_을_테스트한다(){
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();
        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
        int randomOrdinalNumber = (int)( Math.random() * 100 );
        String accessToken = "TEST TOKEN";

        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));

        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
        ExtractableResponse<Response> postPostResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));

        assertThat(postPostResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("빈 제목 게시글 작성")
    public void 빈_제목으로_게시글_작성_을_테스트한다(){
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();
        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
        int randomOrdinalNumber = (int)( Math.random() * 100 );


        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));

        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));

        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");

        PostWriteRequest postWriteRequest = PostWriteRequest.builder()
                .title("")
                .content(testUUID)
                .vaccineType(randomVaccineType)
                .ordinalNumber(randomOrdinalNumber)
                .build();
        ExtractableResponse<Response> postPostResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequest, Map.class));

        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postPostResponse.statusCode()).isEqualTo(HttpStatus.PAYMENT_REQUIRED.value());
    }

    @Test
    @DisplayName("빈 내용 게시글 작성")
    public void 빈_내용으로_게시글_작성_을_테스트한다(){
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();
        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
        int randomOrdinalNumber = (int)( Math.random() * 100 );


        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));

        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));

        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");

        PostWriteRequest postWriteRequest = PostWriteRequest.builder()
                .title(testUUID)
                .content("   ")
                .vaccineType(randomVaccineType)
                .ordinalNumber(randomOrdinalNumber)
                .build();
        ExtractableResponse<Response> postPostResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequest, Map.class));

        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postPostResponse.statusCode()).isEqualTo(HttpStatus.PAYMENT_REQUIRED.value());

    }

    @Test
    @DisplayName("이미지가 없는 게시글 작성 테스트")
    public void 이미지가_없는_게시글_작성_을_테스트한다(){
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();
        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
        int randomOrdinalNumber = (int)( Math.random() * 100 );

        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));

        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));

        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");

        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));

        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.PERMANENT_REDIRECT.value());
    }

    @Test
    @DisplayName("이미지가 있는 게시글 작성 테스트")
    public void 이미지가_있는_게시글_작성_을_테스트한다() throws IOException {
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();
        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
        int randomOrdinalNumber = (int)( Math.random() * 100 );

        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));

        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));

        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");
        Resource resource1 = resourceLoader.getResource("classpath:profileimage/testimage.png");
        Resource resource2 = resourceLoader.getResource("classpath:profileimage/testimage2.png");
        List<File> fileList = new ArrayList<>();
        fileList.add(resource1.getFile());
        fileList.add(resource2.getFile());

        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWriteWithPostImage(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class), fileList);

        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.PERMANENT_REDIRECT.value());
    }

    @Test
    @DisplayName("글 조회시 조회수 증가 테스트")
    public void 글_조회시에_조히수_증가_를_테스트한다(){
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();
        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
        int randomOrdinalNumber = (int)( Math.random() * 100 );

        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));

        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));

        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");

        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));

        Long postId = Long.valueOf(jsonParseUtil.getJsonValue(postPostWriteResponse, "id"));
        ExtractableResponse<Response> getPostByIdResponse = PostRestAssuredCRUD.getPostById(postId);

        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.PERMANENT_REDIRECT.value());
        assertThat(jsonParseUtil.getJsonValue(getPostByIdResponse, "title")).isEqualTo(testUUID);
        assertThat(jsonParseUtil.getJsonValue(getPostByIdResponse, "viewCount")).isEqualTo(String.valueOf(1));

    }

    @Test
    @DisplayName("게시글 제목 수정 테스트")
    public void 게시글_제목_수정_을_테스트한다(){
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();
        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
        int randomOrdinalNumber = (int)( Math.random() * 100 );

        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));

        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));

        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");

        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));

        Long postId = Long.valueOf(jsonParseUtil.getJsonValue(postPostWriteResponse, "id"));

        ExtractableResponse<Response> putPostById = PostRestAssuredCRUD.putPostById(postId, accessToken, objectMapper.convertValue(ModifyPostRequest.builder().title("ge").vaccineType(VaccineType.MODERNA).build(), Map.class));


    }

    @Test
    @DisplayName("게시글 내용 수정 테스트")
    public void 게시글_내용_수정_을_테스트한다(){

    }

    @Test
    @DisplayName("게시글 게시판 수정 테스트")
    public void 게시글_게시판_내용_을_테스트한다(){

    }

    @Test
    @DisplayName("게시글 게시판 수정 테스트")
    public void 게시글_게시판_수정_을_테스트한다(){

    }

    @Test
    @DisplayName("게시글 첨부 이미지 삭제 테스트")
    private void 게시글_첨부_이미지_삭제_를_테스트한다(){

    }
}
