package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageNotFoundException;
import com.teamproject.covid19vaccinereview.domain.ProfileImage;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.ModifyUserRequest;
import com.teamproject.covid19vaccinereview.repository.ProfileImageRepository;
import com.teamproject.covid19vaccinereview.utils.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final ImageFileUtil imageFileUtil;

    @Transactional
    public byte[] findProfileImageById(long id) throws IOException {

        ProfileImage findProfileImage = profileImageRepository.findById(id)
                .orElseThrow(() -> new ProfileImageNotFoundException(""));

        byte[] profileImageData = imageFileUtil.profileImageFileToBytes(findProfileImage.getFileName());

        return profileImageData;
    }

    @Transactional
    public void modify(User findUser, MultipartFile multipartFile, ModifyUserRequest modifyUserRequest) throws IOException {

        if(modifyUserRequest.isWantToChangeProfileImage() && (ObjectUtils.isEmpty(multipartFile) || multipartFile.getOriginalFilename().isBlank())) {   // isWantToChangeProfileImage == True, 기본 이미지로 바꿀 때

            if(findUser.getProfileImage() != null){
                imageFileUtil.deleteProfileImage(findUser.getProfileImage().getFileName());
                profileImageRepository.deleteById(findUser.getProfileImage().getId());
            }

        } else if(modifyUserRequest.isWantToChangeProfileImage() && !ObjectUtils.isEmpty(multipartFile)){ // 프로필 이미지를 다른 이미지로 수정(기본이미지 X)

            String fileExtension = multipartFile.getOriginalFilename().substring( multipartFile.getOriginalFilename().lastIndexOf(".") );

            if(findUser.getProfileImage() == null){

                ProfileImage profileImage = ProfileImage.of(
                        findUser.getEmail() + fileExtension,
                        multipartFile.getSize(),
                        fileExtension
                );
                profileImageRepository.save(profileImage);
                findUser.changeProfileImage(profileImage);

                imageFileUtil.deleteProfileImage(findUser.getProfileImage().getFileName());
                imageFileUtil.saveProfileImage(multipartFile, findUser.getProfileImage().getFileName());

            } else {

                findUser.getProfileImage().changeImage(
                        findUser.getEmail() + fileExtension,
                        multipartFile.getSize(),
                        fileExtension
                );

                imageFileUtil.deleteProfileImage(findUser.getProfileImage().getFileName());
                imageFileUtil.saveProfileImage(multipartFile, findUser.getProfileImage().getFileName());
            }

        }
    }

}
