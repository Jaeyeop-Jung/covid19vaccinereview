package com.teamproject.covid19vaccinereview.service.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class ProfileImageUtil {

    @Value("${file.profileimagepath}")
    private String profileImagePath;

    public boolean saveProfileImage(MultipartFile multipartFile) throws IOException {

        String filename = profileImagePath+"/" + multipartFile.getOriginalFilename();

        FileCopyUtils.copy(multipartFile.getBytes(), new File(filename));

        return true;
    }
}
