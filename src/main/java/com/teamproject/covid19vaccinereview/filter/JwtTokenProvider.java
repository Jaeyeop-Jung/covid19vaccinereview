package com.teamproject.covid19vaccinereview.filter;

import com.teamproject.covid19vaccinereview.aop.exception.customException.JwtIllegalArgumentException;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.dto.UserDetailsImpl;
import com.teamproject.covid19vaccinereview.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    private final UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long ACCESS_TOKEN_EXPIRE_TIME = 10 * 60 * 1000L;
    private final Long REFRESH_TOKEN_EXPIRE_TIME = 60 * 60 * 24 * 14 * 1000L;

    /**
     * methodName : generateRefreshToken
     * author : Jaeyeop Jung
     * description : User를 통해서 RefreshToken을 생성하고 반환한다.
     *
     * @param user User Entity
     * @return RefreshToken 정보
     */
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

    /**
     * methodName : generateAccessToken
     * author : Jaeyeop Jung
     * description : User를 통해서 AceesToken 생성하고 반환한다.
     *
     * @param user User Entity
     * @return AccessToken 정보
     */
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

    /**
     * methodName : findUserIdByJwt
     * author : Jaeyeop Jung
     * description : Jwt 토큰에 담긴 Uesr.id를 찾아낸다.
     *
     * @param token Jwt Token
     * @return 토큰에 담긴 User.id
     */
    public String findUserIdByJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        String userId = claims.getSubject();

        return userId;
    }

    /**
     * methodName : validateToken
     * author : Jaeyeop Jung
     * description : Token을 검증한다.
     *
     * @param token Jwt Token
     * @return 검증 결과
     */
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        } catch (NullPointerException ex){
            log.error("JWT RefreshToken is empty");
        }
        return false;
    }

    /**
     * methodName : getAuthentication
     * author : Jaeyeop Jung
     * description : Jwt Token에 담긴 유저 정보를 DB에 검색하고, 해당 유저의 권한처리를 위해 Context에 담는 Authentication 객체를 반환한다.
     *
     * @param token Jwt Token
     * @return Context에 담을 Authentication 객체
     */
    public Authentication getAuthentication(String token){
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(this.findUserIdByJwt(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
