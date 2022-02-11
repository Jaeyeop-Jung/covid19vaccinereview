package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageFileDuplicateException;
import com.teamproject.covid19vaccinereview.domain.PostImage;
import com.teamproject.covid19vaccinereview.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class ImageFileUtil {

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

    public boolean savePostImage(MultipartFile multipartFile, @NotNull String fileName) throws IOException {

        String rootPath = System.getProperty("user.home");
        File folder = new File(rootPath + "/postimage");
        if(!folder.exists())
            folder.mkdir();

        String nameWithPath = folder.getAbsolutePath() + "/" + fileName;

        if(new File(nameWithPath).exists()){
            throw new ProfileImageFileDuplicateException("");
        }

        FileCopyUtils.copy(multipartFile.getBytes(), new File(folder + "/" + fileName));

        return true;
    }

    public boolean deletePostImage(List<String> postImageNameList){

        String rootPath = System.getProperty("user.home");

        for (String postImageName : postImageNameList) {
            File file = new File(rootPath + "/postimage/" + postImageName);
            if(file.exists()){
                file.delete();
            }
        }

        return true;
    }

    public byte[] profileImageFileToBytes(String fileName) throws IOException {

        String filePath = System.getProperty("user.home") + "/profileimage/" + fileName;
        File file = new File(filePath);
        return FileUtil.readAsByteArray(file);
    }

    public byte[] postImageFileToBytes(String fileName) throws IOException {

        String filePath = System.getProperty("user.home") + "/postimage/" + fileName;
        File file = new File(filePath);
        return FileUtil.readAsByteArray(file);
    }
}
