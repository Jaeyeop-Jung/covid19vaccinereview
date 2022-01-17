package com.teamproject.covid19vaccinereview.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.covid19vaccinereview.aop.log.AopLogger;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.dto.UserDetailsImpl;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import com.teamproject.covid19vaccinereview.service.UserDetailsServiceImpl;
import com.teamproject.covid19vaccinereview.service.UserService;
import com.teamproject.covid19vaccinereview.utils.RestAssuredCRUD;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("UserApiController 테스트")
public class UserApiControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final EntityManager em;

    @LocalServerPort
    int port;

    @Autowired
    public UserApiControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, EntityManager em) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.em = em;
    }

//    @BeforeEach
//    public void setUp() {
//        RestAssured.port = port;
//    }

    @DisplayName("Join 에 관리자 유저를 넣는다")
    @Test
    public void joinTest() {
        RestAssured.port = port;

        UserDto userDto = UserDto.builder()
            .nickname("nickname")
            .password("password")
            .userPhoto("userPhoto")
            .email("email")
            .role(UserRole.ROLE_ADMIN).build();

        ExtractableResponse<Response> response = RestAssuredCRUD.postRequest("/join", userDto);
        요청_성공(response);
    }

    @DisplayName("AOP 로 감싼 로깅이 동작하는지 확인한다.")
    @Test
    public void testAspect() {
        UserService userService = mock(UserService.class);
        UserDetailsServiceImpl userDetailsService = mock(UserDetailsServiceImpl.class);

        AspectJProxyFactory factory = new AspectJProxyFactory(new UserApiController(
            userService, userDetailsService
        ));
        factory.addAspect(new AopLogger());
        UserApiController proxy = factory.getProxy();

        UserDto userDto = UserDto.builder()
            .nickname("nickname")
            .password("password")
            .userPhoto("userPhoto")
            .email("email")
            .role(UserRole.ROLE_ADMIN).build();

        proxy.join(userDto);
    }

    private void 요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("join 테스트")
    public void joinTest_POST() throws Exception {

        //given
        String content = objectMapper.writeValueAsString(UserDto.toEntity(
                "joinTest_POST",
                "joinTest_POST",
                "joinTest_POST",
                "joinTest_POST"
        )); // 테스트용 User 값 넣기

        MockHttpServletRequestBuilder requst = MockMvcRequestBuilders
                .post("/join")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON
                ); // 테스트용 Mock Request 만들기

        //when
        mockMvc.perform( requst )   // 생성한 request 실행
                .andExpect( status().isOk() )   // Response Code = OK (200) 인지 확인
                .andDo( print() ); // Response 내용 출력

        //then
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u " +
                        "where u.email = :email", User.class);
        query.setParameter("email", "joinTest_POST");

        List<User> resultList = query.getResultList();

        assertThat(resultList.get(0)).isInstanceOf(User.class); // JPQL을 이용해 email이 joinTest_POST와 같은 User 클래스를 가져와 정상 적으로 반환 되는지 확인

    }

    @Test
    public void loginTest_POST() throws Exception {

        //given
        String content = "" +
                "{\"email\": \"joinTest_POST\"," +
                " \"password\": \"joinTest_POST\"," +
                " \"nickname\": \"joinTest_POST\"," +
                " \"userPhoto\": \"joinTest_POST\"" +
                "}";
                // 테스트용 UserDto 값 넣기

        System.out.println("content = " + content);

        MockHttpServletRequestBuilder requst = MockMvcRequestBuilders
                .post("/loginForm")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON
                ); // 테스트용 Mock Request 만들기

        //when
        mockMvc.perform( requst )   // 생성한 request 실행
                .andExpect( status().isOk() )   // Response Code = OK (200) 인지 확인
                .andDo( print() ); // Response 내용 출력

        //then  로그인 리턴값 확인하기 (추후 추가하기)


    }


}