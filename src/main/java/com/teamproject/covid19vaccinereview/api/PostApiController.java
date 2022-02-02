package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.domain.Post;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.ImageDto;
import com.teamproject.covid19vaccinereview.dto.PostWriteRequestDto;
import com.teamproject.covid19vaccinereview.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;

    @ApiOperation(value = "게시글 작성", notes = "게시글 작성을 위한 VaccineType과 OrdinalNumber 필수 지정")
    @PostMapping("/post/write")
    public PostWriteRequestDto postWrite(
//            HttpServletRequest request,
            @RequestPart PostWriteRequestDto postWriteRequestDto,
//            @RequestPart(required = false) @Nullable MultipartFile multipartFile[]
            MultipartHttpServletRequest request
            ){

        postWriteRequestDto.initPostWriteRequestDto(request.getFileMap());


        return postWriteRequestDto;
    }

}
