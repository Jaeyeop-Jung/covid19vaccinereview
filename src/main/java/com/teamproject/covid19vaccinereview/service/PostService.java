package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.BoardExceptionHandler;
import com.teamproject.covid19vaccinereview.aop.exception.customException.*;
import com.teamproject.covid19vaccinereview.dto.*;
import com.teamproject.covid19vaccinereview.domain.Board;
import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.PostImage;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.repository.BoardRepository;
import com.teamproject.covid19vaccinereview.repository.PostImageRepository;
import com.teamproject.covid19vaccinereview.repository.PostRepository;
import com.teamproject.covid19vaccinereview.utils.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    private final PostImageService postImageService;
    private final ImageFileUtil imageFileUtil;

    @Transactional(readOnly = true)
    public void checkUserAuthorization(HttpServletRequest request, long postId){

        User findUserByAccessToken = userService.getLoginUserByAccessToken(request);
        User writer = postRepository.findById(postId)
                .orElseThrow(() -> new PostLostUserConnectionException("")).getUser();

        if(!findUserByAccessToken.equals(writer)){
            throw new UnAuthorizedUserException("");
        }
    }

    @Transactional
    public PostWriteResponse write(HttpServletRequest request, PostWriteRequest postWriteRequest, List<MultipartFile> multipartFileList) throws IOException {
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
            imageFileUtil.savePostImage(imageDto.getMultipartFile(), imageDto.getFileName());
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
    public PostWriteResponse modifyPost(HttpServletRequest request, long postId, ModifyPostRequest modifyPostRequest, List<MultipartFile> multipartFileList) {
        checkUserAuthorization(request, postId);
        if(multipartFileList != null && !multipartFileList.get(0).isEmpty()){
            modifyPostRequest.initPostWriteRequestDto(multipartFileList);
        }

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(""));

        if(modifyPostRequest.isWantToChangePostImage()) {   // 게시글 이미지 수정

            if(modifyPostRequest.getAttachedImage().isEmpty()){ // 게시글 이미지를 지우고 싶을 때
                List<PostImage> findAllPostImageByPost = postImageRepository.findAllByPost(findPost);

                imageFileUtil.deletePostImage(
                        findAllPostImageByPost.stream()
                                .map(PostImage::getFileName)
                                .collect(Collectors.toList())
                );

                postImageRepository.deleteAllByPostId(postId);

            } else {    // 게시글 이미지를 변경하고 싶을 때

            }

        }

        if(modifyPostRequest.getTitle() != null){   // 게시글 제목 변경

            if(modifyPostRequest.getTitle().isBlank()){
                throw new BlankPostTitleException("");
            }

            findPost.changeTitle(modifyPostRequest.getTitle());
        }

        if(modifyPostRequest.getContent() != null) {    // 게시글 내용 변경

            if(modifyPostRequest.getContent().isBlank()){
                throw new BlankPostContentException("");
            }

            findPost.changeContent(modifyPostRequest.getContent());
        }

        if(modifyPostRequest.getVaccineType() != null || modifyPostRequest.getOrdinalNumber() >= 1 ) {  // 게시글 게시판 변경
            Board findBoard = boardRepository.findByVaccineTypeAndOrdinalNumber(modifyPostRequest.getVaccineType(), modifyPostRequest.getOrdinalNumber())
                    .orElseThrow(() -> new IncorrcetBaordException(""));

            findPost.changeBoard(findBoard);
        }

        return PostWriteResponse.builder()
                .id(postId)
                .location(domainUrl + "/post/" + postId)
                .build();
    }

    @Transactional
    public Map<String, Object> deletePost(long id, HttpServletRequest request){

        User findUser = userService.getLoginUserByAccessToken(request);
        User writer = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("")).getUser();

        if(!writer.equals(findUser)){
            throw new UnAuthorizedUserException("");
        }

        postImageService.deletePostImageByPostId(id);
        postRepository.deleteById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        return map;
    }
}
