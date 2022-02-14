package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.service.ProfileImageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @GetMapping(value = "/profileimage/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ApiOperation(value = "프로필 이미지", notes = "프로필 이미지를 제공하는 api")
    public @ResponseBody byte[] getProfileImage(
            @PathVariable(name = "id") long id,
            HttpServletResponse response
    ) throws IOException {

        return profileImageService.findProfileImageById(id);
    }

}

