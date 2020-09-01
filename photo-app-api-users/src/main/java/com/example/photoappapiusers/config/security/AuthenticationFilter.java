package com.example.photoappapiusers.config.security;

import com.example.photoappapiusers.model.LoginRequestModel;
import com.example.photoappapiusers.model.dto.UserDto;
import com.example.photoappapiusers.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    UserService userService;
    AuthenticationManager authenticationManager;
    String tokenExpiration;
    String tokenSecret;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestModel.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String userName = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);

        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenExpiration)))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
    }
}
