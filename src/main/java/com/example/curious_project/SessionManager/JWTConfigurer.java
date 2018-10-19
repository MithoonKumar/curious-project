package com.example.curious_project.SessionManager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JWTConfigurer {

    public String generateJWT(String userEmail) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        try {
            JwtBuilder jwtBuilder = Jwts.builder().setId(userEmail).signWith(signatureAlgorithm, "secret".getBytes("UTF-8"));
            return jwtBuilder.compact();
        } catch (Exception ex) {
            System.out.println("Error while generating jwt" + ex);
            return null;
        }
    }

    public String decodeJWT(String jwt) {
        try {
            Claims claims = Jwts.parser().setSigningKey("secret".getBytes("UTF-8")).parseClaimsJws(jwt).getBody();
            return claims.getId();
        } catch (Exception ex) {
            System.out.println("Error while decoding jwt");
            return null;
        }
    }
}
