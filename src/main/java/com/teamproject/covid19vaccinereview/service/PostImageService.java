package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.PostImageNotFoundException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.PostNotFoundException;
import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.PostImage;
import com.teamproject.covid19vaccinereview.repository.PostImageRepository;
import com.teamproject.covid19vaccinereview.repository.PostRepository;
import com.teamproject.covid19vaccinereview.utils.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;
    private final ImageFileUtil imageFileUtil;

    @Transactional
    public byte[] findPostImageByFileName(String fileName) throws IOException {

        PostImage findPostImage = postImageRepository.findByFileName(fileName)
                .orElseThrow(() -> new PostImageNotFoundException(""));

        byte[] profileImageData = imageFileUtil.postImageFileToBytes(findPostImage.getFileName());

        return profileImageData;
    }

    @Transactional
    public void deletePostImageByPostId(long id){

        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(""));

        List<PostImage> postImageList = findPost.getPostImageList();
        postImageList.stream()
                .forEach(postImage -> postImageRepository.delete(postImage));
        imageFileUtil.deletePostImageByList(
                postImageList.stream()
                        .map(PostImage::getFileName)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void deletePostImageByFileName(String fileName){

        deletePostImageByFileName(fileName);
        imageFileUtil.deleteProfileImage(fileName);

    }

}
