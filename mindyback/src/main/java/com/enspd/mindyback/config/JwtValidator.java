package com.enspd.mindyback.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

public class JwtValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(JwtConst.JWT_HEADER);

        if (jwt != null) {
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
            try {

                SecretKey key = Keys.hmacShaKeyFor(JwtConst.SECRET_KEY.getBytes());

                Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String email = String.valueOf(claims.get("email"));

                String authorities = String.valueOf("authorities");

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                throw new BadRequestException("--------Token Invalide--------");
            }

        }
        filterChain.doFilter(request, response);
    }
}
