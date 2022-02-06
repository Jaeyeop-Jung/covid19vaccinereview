package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmail(String email);

    List<User> findByNickname(String nickname);

    @Transactional
    long deleteByEmail(String email);
}
