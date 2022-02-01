package com.teamproject.covid19vaccinereview.utils;

import com.teamproject.covid19vaccinereview.aop.exception.customException.ProfileImageFileDuplicateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;


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
}
