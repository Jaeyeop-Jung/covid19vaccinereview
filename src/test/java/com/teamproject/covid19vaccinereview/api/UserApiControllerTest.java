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
import com.teamproject.covid19vaccinereview.service.UserDetailsServiceImpl;
import com.teamproject.covid19vaccinereview.service.UserService;
import com.teamproject.covid19vaccinereview.utils.UserRequestDto;
import com.teamproject.covid19vaccinereview.utils.RestAssuredCRUD;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import javax.persistence.EntityManager;

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
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.UUID;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("UserApiController 테스트")
@ActiveProfiles("local")
public class UserApiControllerTest {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final EntityManager em;
    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;
    private final UserRequestDto userRequestDto;

    @Autowired
    public UserApiControllerTest(ObjectMapper objectMapper, ResourceLoader resourceLoader, EntityManager em, UserRepository userRepository, ProfileImageRepository profileImageRepository, UserRequestDto userRequestDto) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.em = em;
        this.userRepository = userRepository;
        this.profileImageRepository = profileImageRepository;
        this.userRequestDto = userRequestDto;
    }

    @LocalServerPort
    int port;

    @Test
    @DisplayName("originJoin With ProfileFile 테스트")
    public void 프로필_파일을_포함한_originJoin_을_테스트한다() throws Exception {
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = userRequestDto.createJoinRequestWithUUID(testUUID);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");

        ExtractableResponse<Response> response = RestAssuredCRUD.postOriginJoinRequestWithProfileImage(objectMapper.writeValueAsString(joinRequest), resource.getFile());

        assertThat(response.statusCode()).isEqualTo(200);

        userRepository.deleteByEmail(testUUID);
        profileImageRepository.deleteByFileName(testUUID);

        System.out.println("\n");
    }

    @Test
    @DisplayName("originJoin Without ProfileFile 테스트")
    public void 프로필_파일을_포함하지않은_originJoin_을_테스트한다() throws Exception {
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = userRequestDto.createJoinRequestWithUUID(testUUID);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");

        ExtractableResponse<Response> response = RestAssuredCRUD.postOriginJoinRequestWithProfileImage(objectMapper.writeValueAsString(joinRequest), resource.getFile());

        assertThat(response.statusCode()).isEqualTo(200);

        userRepository.deleteByEmail(testUUID);
        profileImageRepository.deleteByFileName(testUUID);

        System.out.println("\n");
    }


    @Test
    @DisplayName("originLogin 테스트")
    public void originLogin_을_테스트한다() throws Exception {
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = userRequestDto.createJoinRequestWithUUID(testUUID);
        Resource resource = resourceLoader.getResource("classpath:profileimage/testimage.png");
        ExtractableResponse<Response> postOriginJoinResponse = RestAssuredCRUD.postOriginJoinRequestWithProfileImage(objectMapper.writeValueAsString(joinRequest), resource.getFile());

        LoginRequest loginRequest = userRequestDto.createLoginReqeustWithUUID(testUUID);
        ExtractableResponse<Response> postOriginLoginResponse = RestAssuredCRUD.getOriginLogin(objectMapper.convertValue(loginRequest, Map.class));


        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(200);
        assertThat(postOriginLoginResponse.statusCode()).isEqualTo(200);

        userRepository.deleteByEmail(testUUID);
        profileImageRepository.deleteByFileName(testUUID);

    }

    @Test
    @DisplayName("정상적인 권한 처리 테스트")
    public void 정상적인_권한_처리_를_테스트한다() throws JsonProcessingException {
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = userRequestDto.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = RestAssuredCRUD.postOriginJoinRequest(objectMapper.writeValueAsString(joinRequest));
        System.out.println("\n");

        LoginRequest loginRequest = userRequestDto.createLoginReqeustWithUUID(testUUID);
        ExtractableResponse<Response> postOriginLoginResponse = RestAssuredCRUD.getOriginLogin(objectMapper.convertValue(loginRequest, Map.class));
        System.out.println("\n");

        String accessToken = postOriginLoginResponse.header("Authorization").split(" ")[1];
        ExtractableResponse<Response> requestWithAccessTokenResponse = RestAssuredCRUD.getRequestWithAccessToken("/user/test", accessToken);
        System.out.println("\n");

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOriginLoginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(requestWithAccessTokenResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());

        userRepository.deleteByEmail(testUUID);
        profileImageRepository.deleteByFileName(testUUID);
    }

    @Test
    @DisplayName("허용하지 않는 권한 테스트")
    public void 허용하지_않는_권한_을_테스트한다() throws JsonProcessingException {
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = userRequestDto.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = RestAssuredCRUD.postOriginJoinRequest(objectMapper.writeValueAsString(joinRequest));
        System.out.println("\n");

        LoginRequest loginRequest = userRequestDto.createLoginReqeustWithUUID(testUUID);
        ExtractableResponse<Response> postOriginLoginResponse = RestAssuredCRUD.getOriginLogin(objectMapper.convertValue(loginRequest, Map.class));
        System.out.println("\n");

        String accessToken = postOriginLoginResponse.header("Authorization").split(" ")[1];
        ExtractableResponse<Response> requestWithAccessTokenResponse = RestAssuredCRUD.getRequestWithAccessToken("/admin/test", accessToken);
        System.out.println("\n");

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOriginLoginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(requestWithAccessTokenResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

        userRepository.deleteByEmail(testUUID);
        profileImageRepository.deleteByFileName(testUUID);
    }

    @Test
    @DisplayName("비정상적인 토큰 테스트")
    public void 비정상적인_토큰_을_테스트한다() throws JsonProcessingException {
        RestAssured.port = port;

        String testUUID = UUID.randomUUID().toString();

        JoinRequest joinRequest = userRequestDto.createJoinRequestWithUUID(testUUID);
        ExtractableResponse<Response> postOriginJoinResponse = RestAssuredCRUD.postOriginJoinRequest(objectMapper.writeValueAsString(joinRequest));
        System.out.println("\n");

        LoginRequest loginRequest = userRequestDto.createLoginReqeustWithUUID(testUUID);
        ExtractableResponse<Response> postOriginLoginResponse = RestAssuredCRUD.getOriginLogin(objectMapper.convertValue(loginRequest, Map.class));
        System.out.println("\n");

        String accessToken = postOriginLoginResponse.header("Authorization").split(" ")[1];
        ExtractableResponse<Response> requestWithAccessTokenResponse = RestAssuredCRUD.getRequestWithAccessToken("/user/test", "Test");
        System.out.println("\n");

        assertThat(postOriginJoinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(postOriginLoginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(requestWithAccessTokenResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

        userRepository.deleteByEmail(testUUID);
        profileImageRepository.deleteByFileName(testUUID);
    }

//    @Test
//    @DisplayName("회원 비밀번호 수정 테스트")
//    public void 회원_비밀번호_수정_를_테스트한다() throws JsonProcessingException {
//        RestAssured.port = port;
//
//        String testUUID = UUID.randomUUID().toString();
//
//        JoinRequest joinRequest = userRequestDto.createJoinRequestWithUUID(testUUID);
//        ExtractableResponse<Response> postOriginJoinResponse = RestAssuredCRUD.postOriginJoinRequest(objectMapper.writeValueAsString(joinRequest));
//        System.out.println("\n");
//
//        String accessToken = postOriginJoinResponse.header("Authorization");
//        userRequestDto.
//
//
//    }


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
        UserDetailsServiceImpl userDetailsService = mock(UserDetailsServiceImpl.class);

        AspectJProxyFactory factory = new AspectJProxyFactory(new UserApiController(
                userService
        ));
        factory.addAspect(new LoggingAspect());
        UserApiController proxy = factory.getProxy();

        UserDto userDto = UserDto.builder()
            .nickname("nickname")
            .password("password")
            .email("email")
            .role(UserRole.ROLE_ADMIN).build();

        // multipart 확인 후 파라미터 테스트 되도록 수정
//        proxy.originRegister(new MockHttpServletResponse(), userDto);
    }

}
