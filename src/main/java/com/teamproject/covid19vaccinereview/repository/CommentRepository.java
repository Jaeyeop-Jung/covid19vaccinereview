package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    

    @Modifying
    @Query("update Comment c set c.likeCount = c.likeCount + 1 where c.id = :id")
    void increaseLikeCount(@Param("id") long id);

    @Query("select c from Comment c " +
            "join fetch c.user left join fetch c.parent " +
            "where c.post.id = :postId " +
            "order by c.parent.id asc nulls first, c.id asc")
    List<Comment> findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(Long postId);

//    boolean s
}
