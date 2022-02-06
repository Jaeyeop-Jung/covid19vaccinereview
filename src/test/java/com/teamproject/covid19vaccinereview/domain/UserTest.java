package com.teamproject.covid19vaccinereview.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("유저 도메인 엔티티 테스트")
@ActiveProfiles("local")
public class UserTest {

    @DisplayName("이메일 형식을 확인하는 메서드 테스트 성공한다.")
    @Test
    void validEmailFormatTest() {
        User.validateEmailFormat("email@goolge.com");
    }

    @DisplayName("이메일 형식을 확인하는 메서드 테스트 실패한다.")
    @Test
    void invalidEmailFormatTest() {
        Assertions.assertThatThrownBy(() -> {
            User.validateEmailFormat("invalidemail.com@");
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
