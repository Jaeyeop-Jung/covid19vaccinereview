package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByTitleContaining(String keyword, Pageable pageable);

    Page<Post> findAllByContentContaining(String keyword, Pageable pageable);

    Page<Post> findAllByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword,Pageable pageable);
}
