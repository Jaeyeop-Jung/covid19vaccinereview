package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.dto.FindPostByIdResponse;
import com.teamproject.covid19vaccinereview.dto.ModifyPostRequest;
import com.teamproject.covid19vaccinereview.dto.PostWriteRequest;
import com.teamproject.covid19vaccinereview.dto.PostWriteResponse;
import com.teamproject.covid19vaccinereview.service.PostService;
import com.teamproject.covid19vaccinereview.utils.BindingParameterUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class PostApiController {

    private final PostService postService;
    private final BindingParameterUtil bindingParameterUtil;


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
     * @param id the id
     * @return the string
     */
    @ApiOperation(value = "선택 계시글 조회", notes = "선택 게시글 조회")
    @GetMapping("/post/{id}")
    public ResponseEntity<FindPostByIdResponse> postFindByPostId(@PathVariable(name = "id") @NotNull long id){

        FindPostByIdResponse findFindPostByIdResponse = postService.findPostById(id);

        return ResponseEntity.ok(findFindPostByIdResponse);
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
    @ApiOperation(value = "게시글 작성", notes = "게시글 작성을 위한 VaccineType과 OrdinalNumber 필수 지정. 헤더에 원하는 계정 accessToken을 꼭 담아주세요(Authorization : Bearer ey...).")
    @PostMapping("/post")
    public ResponseEntity<PostWriteResponse> postWrite(
            @ModelAttribute @Valid PostWriteRequest postWriteRequest,
            BindingResult bindingResult,
            @RequestPart(required = false) @Nullable List<MultipartFile> multipartFileList,
            HttpServletRequest request
            ) {
        bindingParameterUtil.checkParameterBindingException(bindingResult);

        PostWriteResponse postWriteResponse = postService.write(request, postWriteRequest, multipartFileList);

        return new ResponseEntity<>(postWriteResponse, HttpStatus.PERMANENT_REDIRECT);
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글 수정이 가능한 권한을 위해 헤더에 원하는 계정 accessToken을 꼭 담아주세요.")
    @PutMapping("/post")
    public ResponseEntity<FindPostByIdResponse> modifyPost(
            @ModelAttribute @Valid ModifyPostRequest modifyPostRequest,
            BindingResult bindingResult,
            @RequestPart(required = false) @Nullable List<MultipartFile> multipartFileList,
            HttpServletRequest request
    ){
        bindingParameterUtil.checkParameterBindingException(bindingResult);

        FindPostByIdResponse modifiedPostResponse = postService.modifyPost(request, modifyPostRequest, multipartFileList);

        return ResponseEntity.ok(modifiedPostResponse);
    }
}
