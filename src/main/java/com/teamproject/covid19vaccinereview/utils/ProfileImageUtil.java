package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageFileDuplicateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class ProfileImageUtil {

    public boolean saveProfileImage(MultipartFile multipartFile, @NotNull String fileName) throws IOException {

        String rootPath = System.getProperty("user.home");
        File folder = new File(rootPath + "/profileimage");
        if(!folder.exists())
            folder.mkdir();

        String nameWithPath = folder.getAbsolutePath() + "/" + fileName;

        if(new File(nameWithPath).exists()){
            throw new ProfileImageFileDuplicateException("");
        }

        FileCopyUtils.copy(multipartFile.getBytes(), new File(folder + "/" + fileName));

        return true;
    }

    public boolean deleteProfileImage(String fileName){

        String rootPath = System.getProperty("user.home");
        File file = new File(rootPath + "/profileimage/" + fileName);
        if(file.exists()){
            file.delete();
        }

        return true;
    }

    public byte[] fileToBytes(String fileName) throws IOException {

        String filePath = System.getProperty("user.home") + "/profileimage/" + fileName;
        File file = new File(filePath);
        return FileUtil.readAsByteArray(file);
    }
}
