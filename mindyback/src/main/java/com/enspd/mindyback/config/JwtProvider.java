package com.enspd.mindyback.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtProvider {

    SecretKey key = Keys.hmacShaKeyFor(JwtConst.SECRET_KEY.getBytes());

    public String generateToken(Authentication auth) {

        String jwt = Jwts.builder()
                .claim("email", auth.getName())
                .claim("authorities",auth.getAuthorities())
                .signWith(key)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                        .compact();

        return jwt;
    }

    public  String getEmailFromToken(String jwt){
        System.out.println(jwt);
        if ( jwt.startsWith("Bearer ")) {
            // Extract token after "Bearer "
            try {
                jwt =jwt.substring(7);
                // Continue processing with the extracted JWT
            } catch (StringIndexOutOfBoundsException e) {
                // Handle the exception if the token length is less than 7
                System.out.println("Invalid token: " + jwt);
            }
        }
        jwt.substring(7);
        Claims claims = Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }
}
