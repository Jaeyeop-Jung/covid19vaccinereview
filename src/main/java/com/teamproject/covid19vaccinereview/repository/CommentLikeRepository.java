package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.Comment;
import com.teamproject.covid19vaccinereview.domain.CommentLike;
import com.teamproject.covid19vaccinereview.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByUserAndComment(User user, Comment comment);

    List<CommentLike> findAllByUserAndComment(User user, Comment comment);


}
