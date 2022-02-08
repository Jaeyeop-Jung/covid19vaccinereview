package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.domain.LoginProvider;
import com.teamproject.covid19vaccinereview.dto.PostWriteRequest;
import com.teamproject.covid19vaccinereview.service.PostService;
import com.teamproject.covid19vaccinereview.utils.BindingParameterUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class PostApiController {

    @Value("${domain-url}")
    private String domainUrl;

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
    public String postFindByPostId(@PathVariable(name = "id") @NotNull long id){


        return String.valueOf(id);
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
    public ResponseEntity<Map<String, String>> postWrite(
            @ModelAttribute @Valid PostWriteRequest postWriteRequest,
            BindingResult bindingResult,
            @RequestPart(required = false) @Nullable List<MultipartFile> multipartFileList,
            HttpServletRequest request
            ) throws IOException {
        bindingParameterUtil.checkParameterBindingException(bindingResult);

        HttpHeaders responseHeader = new HttpHeaders();
        Map<String, String> responseBody = new HashMap<>();
        
        if(multipartFileList != null && !multipartFileList.get(0).isEmpty()){
            postWriteRequest.initPostWriteRequestDto(multipartFileList);
        }
        String accessToken = request.getHeader("Authorization").split(" ")[1];
        long writeId = postService.write(accessToken, postWriteRequest);

        responseBody.put("location", domainUrl + "/post/" + writeId);

        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.PERMANENT_REDIRECT);
    }

}
