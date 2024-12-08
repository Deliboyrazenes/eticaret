package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private String secretKey = "yourSecretKey"; // Bu anahtarı güvenli bir yerde saklayın.

    public String generateToken(String identifier, String userType) {
        // identifier Customer için email, Seller için phone number olacak
        return Jwts.builder()
                .setSubject(identifier)
                .claim("userType", userType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Token validation error: ", e);
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error extracting username from token: ", e);
            return null;
        }
    }

    public String extractUserType(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return claims.get("userType", String.class);
        } catch (Exception e) {
            log.error("Error extracting userType from token: ", e);
            return null;
        }
    }
}