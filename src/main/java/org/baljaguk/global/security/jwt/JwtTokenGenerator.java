package org.baljaguk.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.handler.LoginException;
import org.baljaguk.global.security.properies.JwtProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private final JwtProperties jwtProperties;

    // 공모자들 서비스의 엑세스 토큰 생성
    public String generateToken(final String id) {
        final Claims claims = Jwts.claims();
        claims.put("memberId", id);

        final Date now = new Date();
        final Date expiredDate = new Date(now.getTime() + jwtProperties.getExpireLength());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // 엑세스 토큰 해독
    public String extractMemberId(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody()
                    .get("memberId")
                    .toString();
        } catch (final Exception error) {
            throw new LoginException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
}
