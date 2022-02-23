package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.PostLike;
import com.teamproject.covid19vaccinereview.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);

    List<PostLike> findAllByUserAndPost(User user, Post post);
}
