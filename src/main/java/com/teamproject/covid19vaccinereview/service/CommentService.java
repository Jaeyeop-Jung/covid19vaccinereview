package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.CommentNotFoundException;
import com.teamproject.covid19vaccinereview.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public void writeComment(long postId){

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
