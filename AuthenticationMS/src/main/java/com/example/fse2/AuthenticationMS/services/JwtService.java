package com.example.fse2.AuthenticationMS.services;

import com.example.fse2.AuthenticationMS.models.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-time-ms}")
    private long expiredTime;
    @Value("${security.jwt.issuer}")
    private String issuer;

    public String createJwtToken(AppUser appUser){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        var keys = Keys.hmacShaKeyFor(keyBytes);

        return Jwts
                .builder()
                .subject(appUser.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer(issuer)
                .expiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(keys)
                .compact();
    }

    public Claims getTokenClaims(String token){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        var keys = Keys.hmacShaKeyFor(keyBytes);

        try{
            var claims = Jwts
                    .parser()
                    .verifyWith(keys)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expDate = claims.getExpiration();
            Date curDate = new Date();
            if (curDate.before(expDate)){
                return claims;
            }
        }catch (Exception ignored){

        }
        return null;
    }

}
