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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("UserApiController ?????????")
@Sql(scripts = "classpath:afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserApiControllerTest {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final CreateUserRequestUtil createUserRequestUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageFileUtil imageFileUtil;
    private final JsonParseUtil jsonParseUtil;

    @Autowired
    public UserApiControllerTest(ObjectMapper objectMapper, ResourceLoader resourceLoader, UserRepository userRepository, ProfileImageRepository profileImageRepository, CreateUserRequestUtil createUserRequestUtil, BCryptPasswordEncoder bCryptPasswordEncoder, ImageFileUtil imageFileUtil, JsonParseUtil jsonParseUtil) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
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

    @Test
    @DisplayName("????????? ???????????? ?????? ???????????? ?????????")
    public void ?????????_?????????_?????????_originJoin_???_???????????????() throws Exception {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");

        ExtractableResponse<Response> response = UserRestAssuredCRUD.postOriginUserWithProfileImage(objectMapper.convertValue(joinRequest, Map.class), resource.getFile());

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ???????????? ?????????")
    public void ?????????_?????????_???_??????????????????_originJoin_???_???????????????() throws Exception {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);

        ExtractableResponse<Response> response = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequest, Map.class));

        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("????????? ?????????")
    public void ?????????_???_???????????????() throws Exception {

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
    @DisplayName("???????????? ?????? ?????? ?????????")
    public void ????????????_??????_??????_???_???????????????() throws JsonProcessingException {

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
    @DisplayName("???????????? ?????? ?????? ?????????")
    public void ????????????_??????_??????_???_???????????????() throws JsonProcessingException {

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
    @DisplayName("??????????????? ?????? ?????????")
    public void ???????????????_??????_???_???????????????() throws JsonProcessingException {

        ExtractableResponse<Response> requestWithAccessTokenResponse = UserRestAssuredCRUD.getWithAccessToken("/user/1", "Test");
        System.out.println("\n");

        assertThat(requestWithAccessTokenResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Transactional
    @DisplayName("?????? ?????????????????? ?????? ?????????")
    public void ??????_??????????????????_??????_???_???????????????() throws IOException {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUserWithProfileImage(objectMapper.convertValue(joinRequest, Map.class), resource.getFile());
        System.out.println("\n");

        String accessToken = jsonParseUtil.getJsonValue(postOriginJoinResponse, "accessToken");
        Resource changedResource = resourceLoader.getResource("classpath:profileimage/testimage2.png");
        ExtractableResponse<Response> changeProfileImageResponse = UserRestAssuredCRUD.patchWithUserInfo(accessToken, null, null, changedResource.getFile(), true);

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changeProfileImageResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(userRepository.findByEmail(testUUID + "@" + testUUID + ".com").get().getProfileImage().getFileSize()).isEqualTo(changedResource.contentLength());
        assertThat(imageFileUtil.profileImageFileToBytes( profileImageRepository.findByFileName(testUUID + "@" + testUUID + ".com" + ".png").get(0).getFileName() ))
                .isEqualTo(FileUtil.readAsByteArray(changedResource.getFile()));
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????????")
    public void ??????_????????????_??????_???_???????????????() throws IOException {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequest, Map.class));
        System.out.println("\n");

        String accessToken = jsonParseUtil.getJsonValue(postOriginJoinResponse, "accessToken");
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");
        ExtractableResponse<Response> blankPasswordTestResponse = UserRestAssuredCRUD.patchWithUserInfo(accessToken, "    ", null, resource.getFile(), false);
        System.out.println("\n");

        ExtractableResponse<Response> changePasswordTestResponse = UserRestAssuredCRUD.patchWithUserInfo(accessToken, "putTest", null, resource.getFile(), false);
        boolean isChanged = bCryptPasswordEncoder.matches("putTest", userRepository.findByEmail(testUUID + "@" + testUUID + ".com").get().getPassword());

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(blankPasswordTestResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(changePasswordTestResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(isChanged).isEqualTo(true);
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????????")
    public void ??????_?????????_??????_???_???????????????() throws IOException {

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = createUserRequestUtil.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = UserRestAssuredCRUD.postOriginUser(objectMapper.convertValue(joinRequest, Map.class));
        System.out.println("\n");

        String accessToken = jsonParseUtil.getJsonValue(postOriginJoinResponse, "accessToken");
        System.out.println("accessToken = " + accessToken);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");
        ExtractableResponse<Response> changeNicknameTestResponse = UserRestAssuredCRUD.patchWithUserInfo(accessToken, null, "putTest", resource.getFile(), false);

        String changedNickname = jsonParseUtil.getJsonValue(changeNicknameTestResponse, "nickname");

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changeNicknameTestResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changedNickname).isEqualTo("putTest");
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    public void ?????????_?????????_??????_??????_???_???????????????(){

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


//    @DisplayName("register ??? ????????? origin user ????????? ?????? ????????????. (??? originJoin")
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
//        ??????_??????(response);
//    }

//    @DisplayName("Join ?????? ????????? ????????? ?????? ??????, ????????? ?????? ????????? ??????.")
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

    @DisplayName("AOP ??? ?????? ????????? ??????????????? ????????????.")
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

        // multipart ?????? ??? ???????????? ????????? ????????? ??????
//        proxy.originRegister(new MockHttpServletResponse(), userDto);
    }

}
