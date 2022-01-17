package com.teamproject.covid19vaccinereview.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserApiController 테스트")
public class UserApiControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final EntityManager em;

    @Autowired
    public UserApiControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, EntityManager em) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.em = em;
    }

    @Test
    @DisplayName("join 테스트")
    public void joinTest_POST() throws Exception {

        //given
        String content = objectMapper.writeValueAsString(UserDto.toEntity(
                "joinTest_POST",
                "joinTest_POST",
                "joinTest_POST",
                "joinTest_POST",
                null,
                null
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
