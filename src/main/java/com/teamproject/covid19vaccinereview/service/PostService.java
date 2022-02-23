package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.*;
import com.teamproject.covid19vaccinereview.domain.*;
import com.teamproject.covid19vaccinereview.dto.*;
import com.teamproject.covid19vaccinereview.repository.BoardRepository;
import com.teamproject.covid19vaccinereview.repository.PostImageRepository;
import com.teamproject.covid19vaccinereview.repository.PostLikeRepository;
import com.teamproject.covid19vaccinereview.repository.PostRepository;
import com.teamproject.covid19vaccinereview.utils.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final PostLikeRepository postLikeRepository;

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
            imageFileUtil.savePostImage(imageDto);
        }


        return PostWriteResponse.builder()
                .id(post.getId())
                .location(domainUrl + "/post/" + post.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public FindPostResponse findPostList(int page){
        Map<String, Object> response = new LinkedHashMap<>();

        Page<Post> findPost = postRepository.findAll(PageRequest.of(page-1, 10, Sort.Direction.DESC, "id"));

        return FindPostResponse.builder()
                .totalPage(findPost.getTotalPages())
                .pagingPostList(PagingPost.convertFrom(findPost.getContent()))
                .build();
    }

    @Transactional
    public FindPostByIdResponse findPostById(long postId){

        if(!postRepository.existsById(postId)){
            throw new PostNotFoundException("");
        }

        postRepository.updateViewCount(postId);

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(""));

        return FindPostByIdResponse.builder()
                .writer(findPost.getUser().getNickname())
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .postImageUrlList(
                        findPost.getPostImageList().stream()
                                .map(postImage -> domainUrl + "/postimage/" + String.valueOf(postImage.getFileName()))
                                .collect(Collectors.toList())
                )
                .viewCount(findPost.getViewCount())
                .likeCount(findPost.getLikeCount())
                .build();
    }

    @Transactional(readOnly = true)
    public FindPostResponse searchPost(PostSearchType postSearchType, String keyword, int page){

        Page<Post> findPost = null;
        PageRequest pageRequest = PageRequest.of(page-1, 10, Sort.Direction.DESC, "id");
        if(postSearchType == PostSearchType.TITLE){
            findPost = postRepository.findAllByTitleContaining(keyword, pageRequest);
        } else if(postSearchType == PostSearchType.CONTENT) {
            findPost = postRepository.findAllByContentContaining(keyword, pageRequest);
        } else if(postSearchType == PostSearchType.TITLE_OR_CONTENT){
            findPost = postRepository.findAllByTitleContainingOrContentContaining(keyword, keyword, pageRequest);
        }

        return FindPostResponse.builder()
                .totalPage(findPost.getTotalPages())
                .pagingPostList(PagingPost.convertFrom(findPost.getContent()))
                .build();
    }

    @Transactional
    public PostWriteResponse modifyPost(HttpServletRequest request, long postId, ModifyPostRequest modifyPostRequest, List<MultipartFile> multipartFileList) throws IOException {
        checkUserAuthorization(request, postId);


        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(""));

        if(modifyPostRequest.isWantToChangePostImage()) {   // 게시글 이미지 수정

            if (multipartFileList != null && !multipartFileList.get(0).isEmpty() && modifyPostRequest.getModifyPostImageList() != null) {
                modifyPostRequest.initPostWriteRequestDto(multipartFileList);
            }

            List<String> savedPostImageFileNameList = postImageRepository.findAllByPost(findPost).stream()
                    .map(PostImage::getFileName)
                    .collect(Collectors.toList());

            if (modifyPostRequest.getModifyPostImageList() == null || modifyPostRequest.getModifyPostImageList().isEmpty()) { // 게시글 이미지 모두 삭제

                List<PostImage> findAllPostImageListByPost = postImageRepository.findAllByPost(findPost);
                imageFileUtil.deletePostImageByList(
                        findAllPostImageListByPost.stream()
                                .map(PostImage::getFileName)
                                .collect(Collectors.toList())
                );

                postImageRepository.deleteAllByPost(findPost);

            } else {    // 게시글 이미지 선택적 삭제

                for (String savedPostImageFileName : savedPostImageFileNameList) {  // 제거할 파일 삭제

                    if (!modifyPostRequest.getModifyPostImageList().contains(savedPostImageFileName)) {
                        postImageRepository.deleteByFileName(savedPostImageFileName);
                        imageFileUtil.deletePostImageByFileName(savedPostImageFileName);
                    }

                }

            }

            if(modifyPostRequest.getModifyPostImageList() != null) {

                for (String savePostImageFileName : modifyPostRequest.getModifyPostImageList()) {   // 추가할 파일 추가

                    if (!savedPostImageFileNameList.contains(savePostImageFileName)) {

                        for (ImageDto imageDto : modifyPostRequest.getAttachedImage()) {

                            if (imageDto.getFileName().equals(savePostImageFileName)) {

                                postImageRepository.save(
                                        PostImage.of(
                                                findPost,
                                                imageDto.getFileName(),
                                                imageDto.getFileSize(),
                                                imageDto.getFileExtension()
                                        )
                                );
                                imageFileUtil.savePostImage(imageDto);

                            }
                        }
                    }
                }
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
    public Map<String, Object> deletePost(HttpServletRequest request, long postId){

        User findUser = userService.getLoginUserByAccessToken(request);
        User writer = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("")).getUser();

        if(!writer.equals(findUser)){
            throw new UnAuthorizedUserException("");
        }

        postImageService.deletePostImageByPostId(postId);
        postRepository.deleteById(postId);

        Map<String, Object> map = new HashMap<>();
        map.put("id", postId);

        return map;
    }

    @Transactional
    public Integer likePost(HttpServletRequest request, long postId){
        User loginUserByAccessToken = userService.getLoginUserByAccessToken(request);
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(""));

        if(postLikeRepository.existsByUserAndPost(loginUserByAccessToken, findPost)){
            throw new AlreadyLikeException("");
        } else {
            postLikeRepository.save( PostLike.of(loginUserByAccessToken, findPost) );
        }

        return postLikeRepository.findAllByUserAndPost(loginUserByAccessToken, findPost).size();
    }
}
