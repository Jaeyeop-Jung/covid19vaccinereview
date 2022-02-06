package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageNotFoundException;
import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.utils.ProfileImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final ProfileImageUtil profileImageUtil;

    public byte[] findProfileImageById(long id) throws IOException {

        Optional<ProfileImage> findProfileImage = profileImageRepository.findById(id);
        if(findProfileImage.isEmpty()){
            throw new ProfileImageNotFoundException("");
        }
        byte[] profileImageData = profileImageUtil.fileToBytes(findProfileImage.get().getFileName());

        return profileImageData;
    }

}
