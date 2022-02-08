package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.PostImageNotFoundException;
import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageNotFoundException;
import com.teamproject.covid19vaccinereview.domain.PostImage;
import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import com.teamproject.covid19vaccinereview.repository.PostImageRepository;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.utils.ProfileImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final ProfileImageUtil profileImageUtil;

    @Transactional
    public byte[] findPostImageById(long id) throws IOException {

        PostImage findPostImage = postImageRepository.findById(id)
                .orElseThrow(() -> new PostImageNotFoundException(""));

        byte[] profileImageData = profileImageUtil.fileToBytes(findPostImage.getFileName());

        return profileImageData;
    }
}
