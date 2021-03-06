package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.dto.*;
import com.teamproject.covid19vaccinereview.service.PostService;
import com.teamproject.covid19vaccinereview.utils.BindingParameterUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Binding;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * description : 전체 게시글을 조회한다.
     *
     * @param page the page
     * @return 전체 페이지와 간략한 글 내용 반환
     */
    @ApiOperation(value = "전체 계시글 조회", notes = "전체 게시글 조회")
    @GetMapping("/post")
    public ResponseEntity<FindAllPostResponse> postFindAll(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "1") @ApiParam(defaultValue = "1") int page
    )
    {
        return ResponseEntity.ok(postService.findPostList(request, page));
    }


    /**
     * methodName : postFindByPostId
     * author : Jaeyeop Jung
     * description : 특정 게시글을 조회한다. 리턴값은 FE와 상의 후에 어떻게 리턴 해줄지 정함.
     *
     * @param postId the id
     * @return the string
     */
    @ApiOperation(value = "선택 계시글 조회", notes = "선택 게시글 조회")
    @GetMapping("/post/{postId}")
    public ResponseEntity<FindPostByIdResponse> postFindByPostId(
            HttpServletRequest request,
            @PathVariable(name = "postId") @NotNull long postId
    )
    {
        return ResponseEntity.ok(postService.findPostById(request, postId));
    }

    /**
     * methodName : searchPost
     * author : Jaeyeop Jung
     * description : *
     *
     * @param postSearchType 주제, 내용, 주제OR내용 중 선택
     * @param keyword        the keyword
     * @param page           the page
     * @return 전체 페이지와 간략한 글 내용 반환
     */
    @ApiOperation(value = "게시글 검색", notes = "게시글 주제, 내용으로 검색을 하는 기능")
    @GetMapping("/post/search")
    public ResponseEntity<FindAllPostResponse> searchPost(
            HttpServletRequest request,
            @RequestParam @Valid @NotNull PostSearchType postSearchType,
            @RequestParam @NotBlank String keyword,
            @RequestParam(required = false, defaultValue = "1") @ApiParam(defaultValue = "1") int page
    )
    {
        return ResponseEntity.ok(postService.searchPost(request, postSearchType, keyword, page));
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
            ) throws IOException {
        bindingParameterUtil.checkParameterBindingException(bindingResult);

        return ResponseEntity.ok(postService.write(request, postWriteRequest, multipartFileList));
    }

    /**
     * methodName : modifyPost
     * author : Jaeyeop Jung
     * description : *
     *
     * @param postId                삭제할 Post의 ID
     * @param modifyPostRequest 원하는 수정 내용
     * @param bindingResult     the binding result
     * @param multipartFileList 이미지 파일 수정 시에 첨부
     * @param request           토큰 확인용
     * @return the response entity
     */
    @ApiOperation(value = "게시글 수정", notes = "게시글 수정이 가능한 권한을 위해 헤더에 원하는 계정 accessToken을 꼭 담아주세요(Authorization : Bearer ey...).")
    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostWriteResponse> modifyPost(
            @PathVariable(name = "postId") @NotNull long postId,
            @ModelAttribute @Valid ModifyPostRequest modifyPostRequest,
            BindingResult bindingResult,
            @RequestPart(required = false) @Nullable List<MultipartFile> multipartFileList,
            HttpServletRequest request
    ) throws IOException {
        bindingParameterUtil.checkParameterBindingException(bindingResult);

        return ResponseEntity.ok(postService.modifyPost(request, postId, modifyPostRequest, multipartFileList));
    }

    /**
     * methodName : deletePost
     * author : Jaeyeop Jung
     * description : *
     *
     * @param postId      삭제할 Post의 ID
     * @param request the request
     * @return 삭제한 Post ID
     */
    @ApiOperation(value = "게시글 삭제", notes = "게시글 삭제가 가능한 권한을 위해 헤더에 원하는 계정 accessToken을 꼭 담아주세요(Authorization : Bearer ey...).")
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Map<String, Object>> deletePost(
            @PathVariable(name = "postId") @NotNull long postId,
            HttpServletRequest request
    )
    {
        Map<String, Object> response = postService.deletePost(request, postId);

        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "게시글 좋아요", notes = "게시글 좋아요 기능. 권한을 위해 헤더에 원하는 계정 accessToken을 꼭 담아주세요(Authorization : Bearer ey...).")
    @PatchMapping("/post/{postId}/like")
    public ResponseEntity<Integer> likePost(
            HttpServletRequest request,
            @PathVariable(name = "postId") @NotNull long postId
    )
    {
        return ResponseEntity.ok(postService.likePost(request, postId));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> missingServletRequestParameterExceptionHandler(Exception e){
        HttpHeaders responseHeader = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.PAYMENT_REQUIRED;

        log.info("Advice : missingServletRequestParameterExceptionHandler");

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "402");
        map.put("message", "필수 파라미터를 다시 확인해주세요 : "
                + e.getMessage().substring( e.getMessage().indexOf("'") + 1, e.getMessage().lastIndexOf("'")));

        return new ResponseEntity<>(map, responseHeader, httpStatus);
    }
}
