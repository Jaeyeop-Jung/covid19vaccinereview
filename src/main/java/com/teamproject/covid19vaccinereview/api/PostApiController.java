package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.dto.PostWriteRequest;
import com.teamproject.covid19vaccinereview.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class PostApiController {

    private final PostService postService;


    /**
     * methodName : postFindAll
     * author : Jaeyeop Jung
     * description : 전체 게시글을 조회한다. 리턴값은 FE와 상의 후에 어떻게 리턴 해줄지 정함.
     */
    @ApiOperation(value = "전체 계시글 조회", notes = "전체 게시글 조회")
    @GetMapping("/post")
    public void postFindAll(){

    }


    /**
     * methodName : postFindByPostId
     * author : Jaeyeop Jung
     * description : 특정 게시글을 조회한다. 리턴값은 FE와 상의 후에 어떻게 리턴 해줄지 정함.
     *
     * @param postId the post id
     */
    @ApiOperation(value = "선택 계시글 조회", notes = "선택 게시글 조회")
    @GetMapping("/post/{id}")
    public void postFindByPostId(@RequestParam long postId){

    }

    /**
     * methodName : postWrite
     * author : Jaeyeop Jung
     * description : *
     *
     * @param postWriteRequest  the post write request
     * @param multipartFileList the multipart file list
     * @return the post write request
     */
    @ApiOperation(value = "게시글 작성", notes = "게시글 작성을 위한 VaccineType과 OrdinalNumber 필수 지정")
    @PostMapping("/post")
    public PostWriteRequest postWrite(
            @RequestPart PostWriteRequest postWriteRequest,
            @RequestPart List<MultipartFile> multipartFileList
            ){

        postWriteRequest.initPostWriteRequestDto(multipartFileList);
        postService.write(postWriteRequest);

        return postWriteRequest;
    }


}
