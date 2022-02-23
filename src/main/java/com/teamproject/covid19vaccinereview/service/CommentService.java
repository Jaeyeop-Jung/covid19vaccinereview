package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.CommentNotFoundException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.CommentNotMatchedException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.PostNotFoundException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.UnAuthorizedUserException;
import com.teamproject.covid19vaccinereview.domain.Comment;
import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.CommentResponse;
import com.teamproject.covid19vaccinereview.dto.CommentWriteRequest;
import com.teamproject.covid19vaccinereview.repository.CommentRepository;
import com.teamproject.covid19vaccinereview.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final UserService userService;

    @Transactional
    public void writeComment(HttpServletRequest request, long postId, CommentWriteRequest commentWriteRequest){
        User loginUserByAccessToken = userService.getLoginUserByAccessToken(request);
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(""));

        commentRepository.save(
                Comment.of(
                        findPost,
                        loginUserByAccessToken,
                        commentRepository.findById(commentWriteRequest.getParentId())
                                .orElse(null),
                        commentWriteRequest.getContent()
                )
        );


    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByPostId(long postId){
        return CommentResponse.toResponseList(commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(postId));
    }

    @Transactional
    public void modifyComment(long commentId){

    }

    @Transactional
    public String deleteCommentById(HttpServletRequest request, long postId, long commentId){
        User loginUserByAccessToken = userService.getLoginUserByAccessToken(request);


        return null;
    }

    @Transactional
    public Integer likeComment(HttpServletRequest request, long postId, long commentId){

        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(""));

        if(findComment.getPost().getId() != postId){
            throw new CommentNotMatchedException("");
        }

        commentRepository.increaseLikeCount(commentId);

        return commentRepository.findById(commentId).get().getLikeCount() + 1; // 쿼리가 1개 더 나가는 것(데이터 정확성) vs 자체적으로 +1을 해서 반환하는 것(처리 속도)
    }

}
