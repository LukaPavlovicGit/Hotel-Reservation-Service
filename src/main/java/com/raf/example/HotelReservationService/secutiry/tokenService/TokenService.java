package com.raf.example.HotelReservationService.secutiry.tokenService;

import io.jsonwebtoken.Claims;

public interface TokenService {

    String generate(Claims claims);

    Claims parseToken(String jwt);
}
