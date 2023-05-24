package com.rahul.book.Library.component;

import com.rahul.book.Library.service.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Component
public class JwtToken implements Serializable {
    @Serial
    private static final long serialVersionUID = -2550185165626007488L;
    public static long JWT_TOKEN_VALIDITY = 7L;
    @Value("${jwt.secret}")
    private String secret;

    public JwtToken() {
    }

    public String generateNewToken(String username) {
        return Jwts.builder().setClaims(new HashMap())
                .setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, this.secret).compact();
    }

    public String getUserFromToken(String token) {
        Claims claims = (Claims)Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        boolean isValid = expiration.after(new Date());
        if (isValid) {
            return claims.getSubject();
        } else {
            throw new TokenExpiredException();
        }
    }

    static {
        JWT_TOKEN_VALIDITY = TimeUnit.DAYS.toMillis(7L);
    }
}
