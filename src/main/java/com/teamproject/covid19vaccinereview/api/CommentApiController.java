package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.dto.CommentResponse;
import com.teamproject.covid19vaccinereview.dto.CommentWriteRequest;
import com.teamproject.covid19vaccinereview.service.CommentService;
import com.teamproject.covid19vaccinereview.utils.BindingParameterUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentApiController {

    private final CommentService commentService;
    private final BindingParameterUtil bindingParameterUtil;

    @ApiOperation(value = "댓글 작성", notes = "댓글 작성 기능. 댓글 작성을 위해 헤더에 원하는 계정 accessToken을 꼭 담아주세요(Authorization : Bearer ey...).")
    @PostMapping("/post/{postId}/comment")
    public void writeComment(
            HttpServletRequest request,
            @PathVariable(name = "postId") @NotNull long postId,
            @RequestBody @Valid CommentWriteRequest commentWriteRequest,
            BindingResult bindingResult
    )
    {
        bindingParameterUtil.checkParameterBindingException(bindingResult);

        commentService.writeComment(request, postId, commentWriteRequest);

    }

    @ApiOperation(value = "댓글 조회", notes = "댓글 조회 기능")
    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<List<CommentResponse>> findCommentByPostId(
            @PathVariable(name = "postId") @NotNull long postId
    ){
        return ResponseEntity.ok(commentService.findByPostId(postId));
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제 기능. 댓글 작성을 위해 헤더에 원하는 계정 accessToken을 꼭 담아주세요(Authorization : Bearer ey...).")
    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<String> deleteCommentById(
            HttpServletRequest request,
            @PathVariable(name = "postId") @NotNull long postId,
            @PathVariable(name = "commentId") @NotNull long commentId
    )
    {
        return ResponseEntity.ok(commentService.deleteCommentById(request, postId, commentId));
    }

    @ApiOperation(value = "댓글 좋아요", notes = "댓글 좋아요 기능")
    @PatchMapping("/post/{postId}/comment/{commentId}/like")
    public ResponseEntity<Integer> likePost(
            HttpServletRequest request,
            @PathVariable(name = "postId") @NotNull long postId,
            @PathVariable(name = "commentId") @NotNull long commentId
    )
    {
        return ResponseEntity.ok(commentService.likeComment(request, postId, commentId));
    }


}
