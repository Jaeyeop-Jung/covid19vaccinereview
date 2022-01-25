package com.teamproject.covid19vaccinereview.db;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("DB 연결 테스트")
public class DbConnectTest {

    private final EntityManager em;
    private final UserRepository userRepository;

    @Autowired
    public DbConnectTest(EntityManager em, UserRepository userRepository) {
        this.em = em;
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("DB Insert와 Auditing 테스트")
    public void 데이터베이스_연결_테스트(){

        //given
        User user = User.of(
                "데이터베이스_연결_테스트",
                "데이터베이스_연결_테스트",
                UserRole.ROLE_USER,
                LoginProvider.ORIGINAL,
                "데이터베이스_연결_테스트",
                null,
                null
        );
        em.persist(user);
        em.flush();
        em.clear();

        //when
        List<User> findUserList = userRepository.findByEmail("데이터베이스_연결_테스트");
        User findUser = findUserList.get(0);

        findUser.changeNickname("데이터베이스_연결_테스트_완료");
        em.flush();
        em.clear();

        //then
        assertThat(user.getEmail()).isEqualTo(findUser.getEmail()); // email 값은 변경하지 않았기 때문에 같아야한다
        assertThat(user.getLastUpdated()).isNotEqualTo(findUser.getLastUpdated()); // 변경된 시간이 달라야한다

    }

}
