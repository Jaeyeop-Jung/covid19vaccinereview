package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.*;
import com.teamproject.covid19vaccinereview.dto.*;
import com.teamproject.covid19vaccinereview.domain.Board;
import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.PostImage;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.repository.BoardRepository;
import com.teamproject.covid19vaccinereview.repository.PostImageRepository;
import com.teamproject.covid19vaccinereview.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    @Value("${domain-url}")
    private String domainUrl;

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public void checkUserAuthorization(HttpServletRequest request, ModifyPostRequest modifyPostRequest){

        User findUserByAccessToken = userService.getLoginUserByAccessToken(request);
        User writer = postRepository.findById(modifyPostRequest.getId())
                .orElseThrow(() -> new PostLostUserConnectionException("")).getUser();

        if(!findUserByAccessToken.equals(writer)){
            throw new UnAuthorizedUserException("");
        }
    }

    @Transactional
    public PostWriteResponse write(HttpServletRequest request, PostWriteRequest postWriteRequest, List<MultipartFile> multipartFileList) {
        User findUser = userService.getLoginUserByAccessToken(request);

        if(multipartFileList != null && !multipartFileList.get(0).isEmpty()){
            postWriteRequest.initPostWriteRequestDto(multipartFileList);
        }

        Board findBoard = boardRepository.findByVaccineTypeAndOrdinalNumber(postWriteRequest.getVaccineType(), postWriteRequest.getOrdinalNumber())
                .orElseThrow(() -> new IncorrcetBaordException(""));

        Post post = Post.of(
                findUser,
                findBoard,
                postWriteRequest.getTitle(),
                postWriteRequest.getContent()
        );
        postRepository.save(post);

        for (ImageDto imageDto : postWriteRequest.getAttachedImage()) {
            postImageRepository.save(
                    PostImage.of(
                            post,
                            imageDto.getFileName(),
                            imageDto.getFileSize(),
                            imageDto.getFileExtension()
                    )
            );
        }

        return PostWriteResponse.builder()
                .id(post.getId())
                .location(domainUrl + "/post/" + post.getId())
                .build();
    }

    @Transactional
    public FindPostByIdResponse findPostById(long id){

        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(""));

        findPost.updateViewCount();

        return FindPostByIdResponse.builder()
                .writer(findPost.getUser().getNickname())
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .postImageUrlList(
                        findPost.getPostImageList().stream()
                                .map(postImage -> domainUrl + "/postimage/" + String.valueOf(postImage.getId()))
                                .collect(Collectors.toList())
                )
                .viewCount(findPost.getViewCount())
                .likeCount(findPost.getLikeCount())
                .build();
    }

    @Transactional
    public FindPostByIdResponse modifyPost(HttpServletRequest request, ModifyPostRequest modifyPostRequest, List<MultipartFile> multipartFileList) {

        checkUserAuthorization(request, modifyPostRequest);
        if(multipartFileList != null && !multipartFileList.get(0).isEmpty()){
            modifyPostRequest.initPostWriteRequestDto(multipartFileList);
        }



        return FindPostByIdResponse.builder().build();
    }
}
