package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageFileDuplicateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

@Component
public class ProfileImageUtil {

    @Value("${file.profileimagepath}")
    private String profileImagePath;


    public boolean saveProfileImage(MultipartFile multipartFile, @NotNull String fileName) throws IOException {

        String nameWithPath = profileImagePath + "/" + fileName;

        if(new File(nameWithPath).exists()){
            throw new ProfileImageFileDuplicateException("");
        }

        FileCopyUtils.copy(multipartFile.getBytes(), new File(nameWithPath));

        return true;
    }
}
