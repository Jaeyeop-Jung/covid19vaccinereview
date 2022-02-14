package com.teamproject.covid19vaccinereview.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.covid19vaccinereview.aop.LoggingAspect;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import com.teamproject.covid19vaccinereview.dto.LoginRequest;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import com.teamproject.covid19vaccinereview.service.UserService;
import com.teamproject.covid19vaccinereview.utils.*;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import javax.persistence.EntityManager;

import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("UserApiController 테스트")
public class UserApiControllerTest {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final EntityManager em;
    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final CreateUserRequestUtil createUserRequestUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageFileUtil imageFileUtil;
    private final JsonParseUtil jsonParseUtil;

    @Autowired
    public UserApiControllerTest(ObjectMapper objectMapper, ResourceLoader resourceLoader, EntityManager em, UserRepository userRepository, ProfileImageRepository profileImageRepository, CreateUserRequestUtil createUserRequestUtil, BCryptPasswordEncoder bCryptPasswordEncoder, ImageFileUtil imageFileUtil, JsonParseUtil jsonParseUtil) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.em = em;
        this.userRepository = userRepository;
        this.profileImageRepository = profileImageRepository;
        this.createUserRequestUtil = createUserRequestUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.imageFileUtil = imageFileUtil;
        this.jsonParseUtil = jsonParseUtil;
    }

    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    @AfterEach
    void afterEach(){
        userRepository.deleteAll();
        profileImageRepository.deleteAll();
    }


    @Test
    @DisplayName("프로필 이미지가 있는 회원가입 테스트")
    public void 프로필_파일을_포함한_originJoin_을_테스트한다() throws Exception {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");

        ExtractableResponse<Response> response = UserRestAssuredCRUD.postOriginUserWithProfileImage(objectMapper.convertValue(joinRequest, Map.class), resource.getFile());

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("프로필 이미지가 없는 회원가입 테스트")
    public void 프로필_이미지_를_포함하지않은_originJoin_을_테스트한다() throws Exception {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);

        ExtractableResponse<Response> response = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequest, Map.class));

        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("로그인 테스트")
    public void 로그인_을_테스트한다() throws Exception {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUserWithProfileImage(objectMapper.convertValue(joinRequest, Map.class), resource.getFile());

        LoginRequest loginRequest = createUserRequestUtil.createLoginReqeustWithUUID(testUUID);
        ExtractableResponse<Response> postOriginLoginResponse = UserRestAssuredCRUD.getOriginLogin(objectMapper.convertValue(loginRequest, Map.class));


        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(200);
        assertThat(postOriginLoginResponse.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("정상적인 권한 처리 테스트")
    public void 정상적인_권한_처리_를_테스트한다() throws JsonProcessingException {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequest, Map.class));
        System.out.println("\n");

        LoginRequest loginRequest = createUserRequestUtil.createLoginReqeustWithUUID(testUUID);
        ExtractableResponse<Response> postOriginLoginResponse = UserRestAssuredCRUD.getOriginLogin(objectMapper.convertValue(loginRequest, Map.class));
        System.out.println("\n");

        String accessToken = jsonParseUtil.getJsonValue(postOriginJoinResponse, "accessToken");
        ExtractableResponse<Response> requestWithAccessTokenResponse = UserRestAssuredCRUD.getWithAccessToken("/user/test", accessToken);
        System.out.println("\n");

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOriginLoginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(requestWithAccessTokenResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("허용하지 않는 권한 테스트")
    public void 허용하지_않는_권한_을_테스트한다() throws JsonProcessingException {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequest, Map.class));
        System.out.println("\n");

        LoginRequest loginRequest = createUserRequestUtil.createLoginReqeustWithUUID(testUUID);
        ExtractableResponse<Response> postOriginLoginResponse = UserRestAssuredCRUD.getOriginLogin(objectMapper.convertValue(loginRequest, Map.class));
        System.out.println("\n");

        String accessToken = jsonParseUtil.getJsonValue(postOriginJoinResponse, "accessToken");
        ExtractableResponse<Response> requestWithAccessTokenResponse = UserRestAssuredCRUD.getWithAccessToken("/admin/test", accessToken);
        System.out.println("\n");

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOriginLoginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(requestWithAccessTokenResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("비정상적인 토큰 테스트")
    public void 비정상적인_토큰_을_테스트한다() throws JsonProcessingException {

        ExtractableResponse<Response> requestWithAccessTokenResponse = UserRestAssuredCRUD.getWithAccessToken("/user/1", "Test");
        System.out.println("\n");

        assertThat(requestWithAccessTokenResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("회원 비밀번호 수정 테스트")
    public void 회원_비밀번호_수정_을_테스트한다() throws IOException {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequest, Map.class));
        System.out.println("\n");

        String accessToken = jsonParseUtil.getJsonValue(postOriginJoinResponse, "accessToken");
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");
        ExtractableResponse<Response> blankPasswordTestResponse = UserRestAssuredCRUD.putWithUserInfo(accessToken, "    ", null, resource.getFile(), false);
        System.out.println("\n");

        ExtractableResponse<Response> changePasswordTestResponse = UserRestAssuredCRUD.putWithUserInfo(accessToken, "putTest", null, resource.getFile(), false);
        boolean isChanged = bCryptPasswordEncoder.matches("putTest", userRepository.findByEmail(testUUID + "@" + testUUID + ".com").get().getPassword());

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(blankPasswordTestResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(changePasswordTestResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(isChanged).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 닉네임 수정 테스트")
    public void 회원_닉네임_수정_을_테스트한다() throws IOException {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequest, Map.class));
        System.out.println("\n");

        String accessToken = jsonParseUtil.getJsonValue(postOriginJoinResponse, "accessToken");
        System.out.println("accessToken = " + accessToken);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");
        ExtractableResponse<Response> changeNicknameTestResponse = UserRestAssuredCRUD.putWithUserInfo(accessToken, null, "putTest", resource.getFile(), false);

        String changedNickname = jsonParseUtil.getJsonValue(changeNicknameTestResponse, "nickname");

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changeNicknameTestResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changedNickname).isEqualTo("putTest");
    }

    @Test
    @Transactional
    @DisplayName("회원 프로필이미지 수정 테스트")
    public void 회원_프로필이미지_수정_을_테스트한다() throws IOException {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUserWithProfileImage(objectMapper.convertValue(joinRequest, Map.class), resource.getFile());
        System.out.println("\n");

        String accessToken = jsonParseUtil.getJsonValue(postOriginJoinResponse, "accessToken");
        Resource changedResource = resourceLoader.getResource("classpath:profileimage/testimage2.png");
        ExtractableResponse<Response> changeProfileImageResponse = UserRestAssuredCRUD.putWithUserInfo(accessToken, null, null, changedResource.getFile(), true);

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changeProfileImageResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(userRepository.findByEmail(testUUID + "@" + testUUID + ".com").get().getProfileImage().getFileSize()).isEqualTo(changedResource.contentLength());
        assertThat(imageFileUtil.profileImageFileToBytes( profileImageRepository.findByFileName(testUUID + "@" + testUUID + ".com" + ".png").get(0).getFileName() ))
                .isEqualTo(FileUtil.readAsByteArray(changedResource.getFile()));
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    public void 토큰을_이용한_회원_삭제_를_테스트한다(){

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequest, Map.class));
        System.out.println("\n");

        String accessToken = jsonParseUtil.getJsonValue(postOriginJoinResponse, "accessToken");
        ExtractableResponse<Response> deleteResponse = UserRestAssuredCRUD.deleteWithAccessToken(accessToken);

        String deletedEmail = jsonParseUtil.getJsonValue(deleteResponse, "email");

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(deletedEmail).isEqualTo(testUUID + "@" + testUUID + ".com");
        assertThat(userRepository.existsByEmail(testUUID + "@" + testUUID + ".com")).isFalse();
    }


//    @DisplayName("register 을 통해서 origin user 관리자 유저 가입한다. (구 originJoin")
//    @Test
//    public void validJoinTest() {
//        RestAssured.port = port;
//
//        JoinRequest joinRequest = JoinRequest.builder()
//            .email("email")
//            .password("password")
//            .loginProvider(LoginProvider.ORIGINAL)
//            .nickname("nickname")
//            .profileImageDto(null)
//            .build();
//
//        ExtractableResponse<Response> response = RestAssuredCRUD.postRequest("/register",
//            joinRequest);
//        요청_성공(response);
//    }

//    @DisplayName("Join 으로 관리자 유저를 넣는 경우, 이메일 형식 오류가 난다.")
//    @Test
//    public void invalidEmailJoinTest() {
//        RestAssured.port = port;
//
//        UserDto userDto = UserDto.builder()
//            .nickname("nickname")
//            .password("password")
//            .email("email")
//            .role(UserRole.ROLE_ADMIN).build();
//
//        ExtractableResponse<Response> response = RestAssuredCRUD
//            .postRequest("/register", userDto);
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//        assertThat(response.body().asString()).isEqualTo("IllegalArgumentException test");
//    }

    @DisplayName("AOP 로 감싼 로깅이 동작하는지 확인한다.")
    @Test
    public void testAspect() {
        UserService userService = mock(UserService.class);
        BindingParameterUtil bindingParameterUtil = mock(BindingParameterUtil.class);

        AspectJProxyFactory factory = new AspectJProxyFactory(new UserApiController(
                userService, bindingParameterUtil
        ));
        factory.addAspect(new LoggingAspect());
        UserApiController proxy = factory.getProxy();

        UserDto userDto = UserDto.builder()
            .nickname("nickname")
            .password("password")
            .email("email@email.com")
            .role(UserRole.ROLE_ADMIN).build();

        // multipart 확인 후 파라미터 테스트 되도록 수정
//        proxy.originRegister(new MockHttpServletResponse(), userDto);
    }

}
