//package com.teamproject.covid19vaccinereview.api;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.teamproject.covid19vaccinereview.domain.Board;
//import com.teamproject.covid19vaccinereview.domain.VaccineType;
//import com.teamproject.covid19vaccinereview.dto.CommentWriteRequest;
//import com.teamproject.covid19vaccinereview.dto.JoinRequest;
//import com.teamproject.covid19vaccinereview.dto.PostWriteRequest;
//import com.teamproject.covid19vaccinereview.repository.BoardRepository;
//import com.teamproject.covid19vaccinereview.repository.CommentRepository;
//import com.teamproject.covid19vaccinereview.repository.PostRepository;
//import com.teamproject.covid19vaccinereview.repository.UserRepository;
//import com.teamproject.covid19vaccinereview.utils.*;
//import io.restassured.RestAssured;
//import io.restassured.response.ExtractableResponse;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.jdbc.Sql;
//
//import javax.persistence.EntityManager;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@DisplayName("CommentApiController 테스트")
//@Sql(scripts = "classpath:afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//public class CommentApiControllerTest {
//
//    private final ObjectMapper objectMapper;
//    private final JsonParseUtil jsonParseUtil;
//    private final CreateUserRequestUtil createUserRequestUtil;
//    private final CreatePostRequestUtil createPostRequestUtil;
//    private final CreateCommentRequestUtil createCommentRequestUtil;
//
//    private final BoardRepository boardRepository;
//    private final CommentRepository commentRepository;
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//
//    private final EntityManager em;
//
//    @Autowired
//    public CommentApiControllerTest(ObjectMapper objectMapper, JsonParseUtil jsonParseUtil, CreateUserRequestUtil createUserRequestUtil, CreatePostRequestUtil createPostRequestUtil, CreateCommentRequestUtil createCommentRequestUtil, BoardRepository boardRepository, CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, EntityManager em) {
//        this.objectMapper = objectMapper;
//        this.jsonParseUtil = jsonParseUtil;
//        this.createUserRequestUtil = createUserRequestUtil;
//        this.createPostRequestUtil = createPostRequestUtil;
//        this.createCommentRequestUtil = createCommentRequestUtil;
//        this.boardRepository = boardRepository;
//        this.commentRepository = commentRepository;
//        this.postRepository = postRepository;
//        this.userRepository = userRepository;
//        this.em = em;
//    }
//
//    @LocalServerPort
//    int port;
//
//    @BeforeEach
//    void beforeEach() {
//        RestAssured.port = port;
//    }
//
//    @Test
//    @DisplayName("댓글 작성 테스트")
//    public void 댓글_작성_을_테스트한다() {
//
//        String testUUID = UUID.randomUUID().toString();
//        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
//        int randomOrdinalNumber = (int) (Math.random() * 100);
//
//        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
//        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));
//
//        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));
//
//        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
//        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");
//
//        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));
//
//        String postId = jsonParseUtil.getJsonValue(postPostWriteResponse, "id");
//
//        CommentWriteRequest commentWriteRequestWithUUID = createCommentRequestUtil.createCommentWriteRequest(testUUID, 0);
//
//        ExtractableResponse<Response> postCommentResponse = CommentRestAssuredCRUD.postComment(accessToken, postId, commentWriteRequestWithUUID);
//
//        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postCommentResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//    }
//
//    @Test
//    @DisplayName("내용이 없는 댓글 작성 테스트")
//    public void 내용이_없는_댓글_작성_을_테스트한다() {
//
//        String testUUID = UUID.randomUUID().toString();
//        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
//        int randomOrdinalNumber = (int) (Math.random() * 100);
//
//        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
//        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));
//
//        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));
//
//        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
//        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");
//
//        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));
//
//        String postId = jsonParseUtil.getJsonValue(postPostWriteResponse, "id");
//
//        CommentWriteRequest commentWriteRequestWithUUID = CommentWriteRequest.builder().parentId(0).build();
//
//        ExtractableResponse<Response> postCommentResponse = CommentRestAssuredCRUD.postComment(accessToken, postId, commentWriteRequestWithUUID);
//
//        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postCommentResponse.statusCode()).isNotEqualTo(HttpStatus.OK.value());
//    }
//
//    @Test
//    @DisplayName("댓글 조회 테스트")
//    public void 댓글_조회_를_테스트한다() {
//
//        String testUUID = UUID.randomUUID().toString();
//        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
//        int randomOrdinalNumber = (int) (Math.random() * 100);
//
//        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
//        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));
//
//        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));
//
//        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
//        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");
//
//        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));
//
//        String postId = jsonParseUtil.getJsonValue(postPostWriteResponse, "id");
//
//        CommentWriteRequest commentWriteRequestWithUUID = createCommentRequestUtil.createCommentWriteRequest(testUUID, 0);
//
//        ExtractableResponse<Response> postCommentResponse = CommentRestAssuredCRUD.postComment(accessToken, postId, commentWriteRequestWithUUID);
//
//        ExtractableResponse<Response> getCommentResponse = CommentRestAssuredCRUD.getComment(accessToken, postId);
//
//        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postCommentResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(getCommentResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//    }
//
//    @Test
//    @DisplayName("댓글 수정 테스트")
//    public void 댓글_수정_을_테스트한다() {
//
//        String testUUID = UUID.randomUUID().toString();
//        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
//        int randomOrdinalNumber = (int) (Math.random() * 100);
//
//        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
//        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));
//
//        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));
//
//        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
//        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");
//
//        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));
//
//        String postId = jsonParseUtil.getJsonValue(postPostWriteResponse, "id");
//
//        CommentWriteRequest commentWriteRequestWithUUID = createCommentRequestUtil.createCommentWriteRequest(testUUID, 0);
//
//        ExtractableResponse<Response> postCommentResponse = CommentRestAssuredCRUD.postComment(accessToken, postId, commentWriteRequestWithUUID);
//
//        String commentId = jsonParseUtil.getJsonValue(postCommentResponse.body(), "id");
//
//        ExtractableResponse<Response> patchCommentResponse = CommentRestAssuredCRUD.patchComment(accessToken, postId, commentId, "{\"content\":\"변경\"}");
//
//        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postCommentResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(patchCommentResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(jsonParseUtil.getJsonValue(patchCommentResponse.body(), "content")).isEqualTo("변경");
//    }
//
//    @Test
//    @DisplayName("빈 내용 댓글 수정 테스트")
//    public void 빈_내용_댓글_수정_을_테스트한다() {
//
//        String testUUID = UUID.randomUUID().toString();
//        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
//        int randomOrdinalNumber = (int) (Math.random() * 100);
//
//        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
//        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));
//
//        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));
//
//        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
//        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");
//
//        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));
//
//        String postId = jsonParseUtil.getJsonValue(postPostWriteResponse, "id");
//
//        CommentWriteRequest commentWriteRequestWithUUID = createCommentRequestUtil.createCommentWriteRequest(testUUID, 0);
//
//        ExtractableResponse<Response> postCommentResponse = CommentRestAssuredCRUD.postComment(accessToken, postId, commentWriteRequestWithUUID);
//
//        String commentId = jsonParseUtil.getJsonValue(postCommentResponse.body(), "id");
//
//        ExtractableResponse<Response> patchCommentResponse = CommentRestAssuredCRUD.patchComment(accessToken, postId, commentId, "{\"content\":\"\"}");
//
//        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postCommentResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(patchCommentResponse.statusCode()).isNotEqualTo(HttpStatus.OK.value());
//    }
//
//    @Test
//    @DisplayName("계층형 대댓글 테스트")
//    public void 계층형_댓글_을_테스트한다() {
//
//        String testUUID = UUID.randomUUID().toString();
//        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
//        int randomOrdinalNumber = (int) (Math.random() * 100);
//
//        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
//        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));
//
//        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));
//
//        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
//        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");
//
//        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));
//
//        String postId = jsonParseUtil.getJsonValue(postPostWriteResponse, "id");
//
//        List<ExtractableResponse<Response>> postCommentResponseList = new ArrayList<>();
//        postCommentResponseList.add(CommentRestAssuredCRUD.postComment(accessToken, postId, createCommentRequestUtil.createCommentWriteRequest("1", 0)));
//        String commentId1 = jsonParseUtil.getJsonValue(postCommentResponseList.get(0).body(), "id");
//        postCommentResponseList.add(CommentRestAssuredCRUD.postComment(accessToken, postId, createCommentRequestUtil.createCommentWriteRequest("2", 0)));
//        String commentId2 = jsonParseUtil.getJsonValue(postCommentResponseList.get(1).body(), "id");
//        postCommentResponseList.add(CommentRestAssuredCRUD.postComment(accessToken, postId, createCommentRequestUtil.createCommentWriteRequest("3", Long.parseLong(commentId1))));
//        String commentId3 = jsonParseUtil.getJsonValue(postCommentResponseList.get(2).body(), "id");
//        postCommentResponseList.add(CommentRestAssuredCRUD.postComment(accessToken, postId, createCommentRequestUtil.createCommentWriteRequest("4", Long.parseLong(commentId1))));
//        postCommentResponseList.add(CommentRestAssuredCRUD.postComment(accessToken, postId, createCommentRequestUtil.createCommentWriteRequest("5", Long.parseLong(commentId3))));
//        postCommentResponseList.add(CommentRestAssuredCRUD.postComment(accessToken, postId, createCommentRequestUtil.createCommentWriteRequest("6", Long.parseLong(commentId2))));
//
//        ExtractableResponse<Response> getCommentResponse = CommentRestAssuredCRUD.getComment(accessToken, postId);
//
//        List<String> childrenCommentIdByJsonArray = jsonParseUtil.getChildrenCommentIdByJsonArray(getCommentResponse.body());
//
//        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        for (ExtractableResponse<Response> responseExtractableResponse : postCommentResponseList) {
//            assertThat(responseExtractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        }
//        assertThat(getCommentResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(childrenCommentIdByJsonArray.get(0)).isEqualTo(commentId1);
//        assertThat(childrenCommentIdByJsonArray.get(1)).isEqualTo(commentId3);
//        assertThat(childrenCommentIdByJsonArray.get(2)).isEqualTo(jsonParseUtil.getJsonValue(postCommentResponseList.get(3), "id"));
//        assertThat(childrenCommentIdByJsonArray.get(3)).isEqualTo(jsonParseUtil.getJsonValue(postCommentResponseList.get(4), "id"));
//        assertThat(childrenCommentIdByJsonArray.get(4)).isEqualTo(commentId2);
//        assertThat(childrenCommentIdByJsonArray.get(5)).isEqualTo(jsonParseUtil.getJsonValue(postCommentResponseList.get(5), "id"));
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 테스트")
//    public void 댓글_삭제_를_테스트한다() {
//
//        String testUUID = UUID.randomUUID().toString();
//        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
//        int randomOrdinalNumber = (int) (Math.random() * 100);
//
//        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
//        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));
//
//        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));
//
//        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
//        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");
//
//        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));
//
//        String postId = jsonParseUtil.getJsonValue(postPostWriteResponse, "id");
//
//        ExtractableResponse<Response> postCommentResponse = CommentRestAssuredCRUD.postComment(accessToken, postId, createCommentRequestUtil.createCommentWriteRequest(testUUID, 0));
//
//        String commentId = jsonParseUtil.getJsonValue(postCommentResponse.body(), "id");
//
//        ExtractableResponse<Response> deleteCommentResponse = CommentRestAssuredCRUD.deleteComment(accessToken, postId, commentId);
//
//        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postCommentResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(deleteCommentResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//    }
//
//    @Test
//    @DisplayName("댓글 좋아요 테스트")
//    public void 댓글_좋아요_를_테스트한다() {
//
//        String testUUID = UUID.randomUUID().toString();
//        VaccineType randomVaccineType = VaccineType.getRandomVaccineType();
//        int randomOrdinalNumber = (int) (Math.random() * 100);
//
//        JoinRequest joinRequestWithUUID = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
//        ExtractableResponse<Response> postUserResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequestWithUUID, Map.class));
//
//        boardRepository.save(Board.of(randomVaccineType, randomOrdinalNumber));
//
//        PostWriteRequest postWriteRequestWithUUID = createPostRequestUtil.createPostWriteRequestWithUUID(testUUID, randomVaccineType, randomOrdinalNumber);
//        String accessToken = jsonParseUtil.getJsonValue(postUserResponse, "accessToken");
//
//        ExtractableResponse<Response> postPostWriteResponse = PostRestAssuredCRUD.postPostWrite(accessToken, objectMapper.convertValue(postWriteRequestWithUUID, Map.class));
//
//        String postId = jsonParseUtil.getJsonValue(postPostWriteResponse, "id");
//
//        ExtractableResponse<Response> postCommentResponse1 = CommentRestAssuredCRUD.postComment(accessToken, postId, createCommentRequestUtil.createCommentWriteRequest(testUUID, 0));
//        ExtractableResponse<Response> postCommentResponse2 = CommentRestAssuredCRUD.postComment(accessToken, postId, createCommentRequestUtil.createCommentWriteRequest(testUUID, 0));
//
//        String commentId1 = jsonParseUtil.getJsonValue(postCommentResponse1.body(), "id");
//        String commentId2 = jsonParseUtil.getJsonValue(postCommentResponse2.body(), "id");
//
//        ExtractableResponse<Response> patchCommentLikeResponse = CommentRestAssuredCRUD.patchCommentLike(accessToken, postId, commentId1);
//
//        ExtractableResponse<Response> getCommentByPostIdResponse = CommentRestAssuredCRUD.getComment(accessToken, postId);
//
//        Map<String, Boolean> commentLikeByJsonArray = jsonParseUtil.getCommentLikeByJsonArray(getCommentByPostIdResponse);
//
//        assertThat(postUserResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postPostWriteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postCommentResponse1.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(postCommentResponse2.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(patchCommentLikeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(commentLikeByJsonArray.get(commentId1)).isTrue();
//        assertThat(commentLikeByJsonArray.get(commentId2)).isFalse();
//    }
//
//}