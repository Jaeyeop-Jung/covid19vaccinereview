package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.*;
import com.teamproject.covid19vaccinereview.domain.Comment;
import com.teamproject.covid19vaccinereview.domain.CommentLike;
import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.CommentResponse;
import com.teamproject.covid19vaccinereview.dto.CommentWriteRequest;
import com.teamproject.covid19vaccinereview.repository.CommentLikeRepository;
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
    private final CommentLikeRepository commentLikeRepository;

    private final UserService userService;

    @Transactional
    public CommentResponse writeComment(HttpServletRequest request, long postId, CommentWriteRequest commentWriteRequest){
        User loginUserByAccessToken = userService.getLoginUserByAccessToken(request);
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(""));

        Comment savedComment = commentRepository.save(
                Comment.of(
                        findPost,
                        loginUserByAccessToken,
                        commentRepository.findById(commentWriteRequest.getParentId())
                                .orElse(null),
                        commentWriteRequest.getContent()
                )
        );

        return CommentResponse.toDto(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByPostId(long postId){
        return CommentResponse.toResponseList(commentRepository.findAllCommentByPostId(postId));
    }

    @Transactional
    public CommentResponse modifyComment(HttpServletRequest request, long commentId, String content){
        User loginUserByAccessToken = userService.getLoginUserByAccessToken(request);
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(""));

        if(!findComment.getUser().equals(loginUserByAccessToken)){
            throw new UnAuthorizedUserException("");
        }

        findComment.changeContent(content);

        return CommentResponse.toDto(findComment);
    }

    @Transactional
    public String deleteCommentById(HttpServletRequest request, long postId, long commentId){
        User loginUserByAccessToken = userService.getLoginUserByAccessToken(request);
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(""));

        if(!findComment.getUser().equals(loginUserByAccessToken)){
            throw new UnAuthorizedUserException("");
        }
        if(!findComment.getPost().getId().equals(postId)){
            throw new CommentNotMatchedException("");
        }

        if(findComment.getChildren().size() == 0){

        } else {
            findComment.setDeleted();
        }

        return null;
    }

    @Transactional
    public Integer likeComment(HttpServletRequest request, long postId, long commentId){
        User loginUserByAccessToken = userService.getLoginUserByAccessToken(request);
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(""));

        if(findComment.getPost().getId() != postId){
            throw new CommentNotMatchedException("");
        }

        if(commentLikeRepository.existsByUserAndComment(loginUserByAccessToken, findComment)){
            throw new AlreadyLikeException("");
        } else {
            commentLikeRepository.save( CommentLike.of(loginUserByAccessToken, findComment) );
        }

        return commentLikeRepository.findAllByUserAndComment(loginUserByAccessToken, findComment).size();
    }

}
