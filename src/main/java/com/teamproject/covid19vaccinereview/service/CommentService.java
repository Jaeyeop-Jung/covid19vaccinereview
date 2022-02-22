package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.CommentNotFoundException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.PostNotFoundException;
import com.teamproject.covid19vaccinereview.domain.Comment;
import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.CommentWriteRequest;
import com.teamproject.covid19vaccinereview.repository.CommentRepository;
import com.teamproject.covid19vaccinereview.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

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
    public void findByPostId(long postId){

    }

    @Transactional
    public void modifyComment(long commentId){

    }

    @Transactional
    public Integer likeComment(long commentId){

        if(commentRepository.existsById(commentId)){
            throw new CommentNotFoundException("");
        }

        commentRepository.increaseLikeCount(commentId);

        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("")).getLikeCount();
    }
}
