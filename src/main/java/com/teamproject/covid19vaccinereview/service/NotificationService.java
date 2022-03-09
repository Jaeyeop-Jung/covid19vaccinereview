package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.aop.exception.customException.JwtIllegalArgumentException;
import com.teamproject.covid19vaccinereview.filter.JwtTokenProvider;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JwtTokenProvider jwtTokenProvider;

    private final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public SseEmitter subscribe(HttpServletRequest request){

        String accessToken = (request.getHeader("Authorization") != null) ?
                request.getHeader("Authorization").split(" ")[1] : null;

        if(accessToken == null){
            throw new JwtIllegalArgumentException("");
        }
        if(!jwtTokenProvider.validateToken(accessToken)){
            throw new MalformedJwtException("");
        }

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("coonect"));
        } catch (IOException e){
            e.printStackTrace();
        }

        Long userIdByJwt = jwtTokenProvider.findUserIdByJwt(accessToken);
        sseEmitterMap.put(userIdByJwt, sseEmitter);

        sseEmitter.onCompletion(() -> {
            sseEmitterMap.remove(userIdByJwt);
            System.out.println("COMPLETITION 발생");
        });
        sseEmitter.onTimeout(() -> {
            sseEmitterMap.remove(userIdByJwt);
            System.out.println("TIMEOUT 발생");
        });
        sseEmitter.onError((e) -> {
            sseEmitterMap.remove(userIdByJwt);
            System.out.println("ERROR 발생");
        });


        return sseEmitter;
    }

    public void memo(String content) throws IOException {
        for (Long aLong : sseEmitterMap.keySet()) {
            SseEmitter sseEmitter = sseEmitterMap.get(aLong);
            sseEmitter.send(SseEmitter.event().name("content").data("받아라 !!!" + content));
        }
        System.out.println("통과!!");
        for (Long aLong : sseEmitterMap.keySet()) {
            System.out.println("aLong = " + aLong);
        }
    }

}
