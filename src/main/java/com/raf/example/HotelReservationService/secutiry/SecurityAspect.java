package com.raf.example.HotelReservationService.secutiry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raf.example.HotelReservationService.dto.PayloadWrapper;
import com.raf.example.HotelReservationService.secutiry.tokenService.TokenService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Base64;


@AllArgsConstructor
@NoArgsConstructor

@Aspect
@Configuration
public class SecurityAspect {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    private TokenService tokenService;

    private Base64.Decoder decoder = Base64.getUrlDecoder();

    public SecurityAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Around("@annotation(com.raf.example.HotelReservationService.secutiry.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //Get method signature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //Check for authorization parameter
        String token = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {

            if (methodSignature.getParameterNames()[i].equals("authorization")) {
                //Check bearer schema
                if (joinPoint.getArgs()[i].toString().startsWith("\"token\"")) {
                    //Get token
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                    token = token.substring(1, token.length()-1);
                }
            }
        }

        //If token is not presents return UNAUTHORIZED response
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //Try to parse token
        Claims claims = tokenService.parseToken(token);
        //If fails return UNAUTHORIZED response
        if (claims == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //Check user role and proceed if user has appropriate role for specified route
        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        String role = claims.get("role", String.class);
        if (Arrays.asList(checkSecurity.roles()).contains(role)) {
            return joinPoint.proceed();
        }
        //Return FORBIDDEN if user hasn't appropriate role for specified route
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public Long getUserId(String authorization){
        String token = authorization.split(" ")[1];
        String payload = token.split("\\.")[1];
        String decodedJSON = new String(decoder.decode(payload));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PayloadWrapper payloadWrapper = gson.fromJson(decodedJSON, PayloadWrapper.class);
        return payloadWrapper.getId();
    }

    public String getUserEmail(String authorization){
        String token = authorization.split(" ")[1];
        String payload = token.split("\\.")[1];
        String decodedJSON = new String(decoder.decode(payload));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PayloadWrapper payloadWrapper = gson.fromJson(decodedJSON, PayloadWrapper.class);
        return payloadWrapper.getEmail();
    }

    public <T> T getUserAttribute(String authorization, String attribute){
        String token = authorization.split(" ")[1];
        String payload = token.split("\\.")[1];
        String decodedJSON = new String(decoder.decode(payload));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PayloadWrapper payloadWrapper = gson.fromJson(decodedJSON, PayloadWrapper.class);
        return (T)payloadWrapper.getAttribute(attribute);
    }

    /*
    // zasto ne je claims null ?
    public Long getUserId(String authorization) {
        String token = authorization.split(" ")[1];
        Claims claims = tokenService.parseToken(token);
        return claims.get("id", Integer.class).longValue();
    }

    public String getUserEmail(String authorization) {
        String token = authorization.split(" ")[1];
        Claims claims = tokenService.parseToken(token);
        return claims.get("email", String.class);
    }
    */

}
