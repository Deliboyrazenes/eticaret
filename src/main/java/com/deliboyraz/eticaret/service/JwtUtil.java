package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.user.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private String secretKey = "yourSecretKey"; // Bu anahtarı güvenli bir yerde saklayın.

    public String generateToken(String email, String userType) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userType", userType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 gün geçerli
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
}
}