package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
