package com.teamproject.covid19vaccinereview.filter;

import com.teamproject.covid19vaccinereview.domain.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long ACCESS_TOKEN_EXPIRE_TIME = 10 * 60 * 1000L;
    private final Long REFRESH_TOKEN_EXPIRE_TIME = 60 * 60 * 24 * 14 * 1000L;

    public String generateRefreshToken(User user){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "REFRESH_TOKEN")
                .setHeaderParam("alg", "HS256")
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .claim("role", user.getRole().toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateAccessToken(User user){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "ACCESS_TOKEN")
                .setHeaderParam("alg", "HS256")
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .claim("role", user.getRole().toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


}
