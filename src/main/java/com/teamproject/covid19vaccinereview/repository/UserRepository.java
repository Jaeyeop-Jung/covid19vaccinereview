package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
