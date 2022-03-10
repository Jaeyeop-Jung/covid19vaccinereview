package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/sub")
    @ApiOperation(value = "SSE 테스트", notes = "SSE를 통해서 백엔드 서버를 계속 구독")
    public SseEmitter sub(HttpServletRequest request){
        return notificationService.subscribe(request);
    }

    @PostMapping("/memo")
    @ApiOperation(value = "SSE 메시지 전송 테스트", notes = "백엔드에 SSE로 연결된 모든 유저에게 메시지 전송")
    public void memo(@RequestParam String content) throws IOException {
        notificationService.memo(content);
        System.out.println("메시지 전송 성공");
    }
}
