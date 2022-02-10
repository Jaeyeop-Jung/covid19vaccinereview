package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.PostImageNotFoundException;
import com.teamproject.covid19vaccinereview.domain.PostImage;
import com.teamproject.covid19vaccinereview.repository.PostImageRepository;
import com.teamproject.covid19vaccinereview.utils.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final ImageFileUtil imageFileUtil;

    @Transactional
    public byte[] findPostImageById(long id) throws IOException {

        PostImage findPostImage = postImageRepository.findById(id)
                .orElseThrow(() -> new PostImageNotFoundException(""));

        byte[] profileImageData = imageFileUtil.postImageFileToBytes(findPostImage.getFileName());

        return profileImageData;
    }
}
