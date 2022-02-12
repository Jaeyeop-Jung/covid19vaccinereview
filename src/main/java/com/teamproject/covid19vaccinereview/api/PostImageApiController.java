package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.service.PostImageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostImageApiController {

    private final PostImageService postImageService;

    @ApiOperation(value = "게시글 이미지", notes = "게시글 이미지를 제공하는 api")
    @GetMapping(value = "/postimage/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] getProfileImage(
            @PathVariable(name = "id") long id,
            HttpServletResponse response) throws IOException {

        return postImageService.findPostImageById(id);
    }
}
