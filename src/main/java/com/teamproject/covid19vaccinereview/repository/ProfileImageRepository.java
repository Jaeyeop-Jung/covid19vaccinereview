package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    List<ProfileImage> findByFileName(String fileName);

    @Transactional
    long deleteByFileName(String fileName);
}
