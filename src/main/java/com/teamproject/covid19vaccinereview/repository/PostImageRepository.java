package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
