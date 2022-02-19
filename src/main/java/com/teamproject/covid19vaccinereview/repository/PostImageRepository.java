package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    public List<PostImage> findAllByPost(Post post);

    public Optional<PostImage> findByFileName(String fileName);

    public boolean existsByFileName(String fileName);

    @Transactional
    public void deleteAllByPost(Post post);

    @Transactional
    public void deleteByFileName(String fileName);

}
