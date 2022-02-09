package com.teamproject.covid19vaccinereview.db;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.UserRole;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("DB 연결 테스트")
public class DbConnectTest {

    private final EntityManager em;
    private final UserRepository userRepository;
    private final ProfileImageRepository profileImageRepository;

    @Autowired
    public DbConnectTest(EntityManager em, UserRepository userRepository, ProfileImageRepository profileImageRepository) {
        this.em = em;
        this.userRepository = userRepository;
        this.profileImageRepository = profileImageRepository;
    }

    @Test
    @DisplayName("DB Insert와 Auditing 테스트")
    public void 데이터베이스_연결_테스트(){

        //given
        ProfileImage profileImage = ProfileImage.of(
                "데이터베이스_연결_테스트",
                1L,
                "데이터베이스_연결_테스트"
        );

        profileImageRepository.save(profileImage);

        User user = User.of(
                "데이터베이스_연결_테스트",
                "데이터베이스_연결_테스트",
                UserRole.ROLE_USER,
                LoginProvider.ORIGINAL,
                "데이터베이스_연결_테스트",
                profileImage,
                null
        );
        em.persist(user);
        em.flush();
        em.clear();

        //when
        Optional<User> findUser = userRepository.findByEmail("데이터베이스_연결_테스트");

        findUser.get().changeNickname("데이터베이스_연결_테스트_완료");
        em.flush();
        em.clear();

        //then
        assertThat(user.getEmail()).isEqualTo(findUser.get().getEmail()); // email 값은 변경하지 않았기 때문에 같아야한다
        assertThat(user.getLastUpdated()).isNotEqualTo(findUser.get().getLastUpdated()); // 변경된 시간이 달라야한다

    }

}
