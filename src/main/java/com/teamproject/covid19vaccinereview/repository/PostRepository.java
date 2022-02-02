package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
