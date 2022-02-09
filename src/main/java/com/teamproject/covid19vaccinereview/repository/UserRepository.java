package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByNickname(String nickname);

    @Transactional
    long deleteByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
