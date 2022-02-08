package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageNotFoundException;
import com.teamproject.covid19vaccinereview.domain.ProfileImage;
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
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final ProfileImageUtil profileImageUtil;

    @Transactional
    public byte[] findProfileImageById(long id) throws IOException {

        ProfileImage findProfileImage = profileImageRepository.findById(id)
                .orElseThrow(() -> new ProfileImageNotFoundException(""));

        byte[] profileImageData = profileImageUtil.fileToBytes(findProfileImage.getFileName());

        return profileImageData;
    }

}
