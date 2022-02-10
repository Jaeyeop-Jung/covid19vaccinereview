package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    public List<PostImage> findAllByPost(Post post);

    @Transactional
    public void deleteAllByPostId(long postId);

}
