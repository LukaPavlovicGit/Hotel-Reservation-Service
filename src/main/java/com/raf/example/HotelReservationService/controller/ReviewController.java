package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.dto.ReviewDto;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private SecurityAspect securityAspect;
    private ReviewService reviewService;

    public ReviewController(SecurityAspect securityAspect, ReviewService reviewService) {
        this.securityAspect = securityAspect;
        this.reviewService = reviewService;
    }

    @PostMapping("/comment")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @ApiOperation(value = "manager registration")
    public ResponseEntity<ReviewDto> registerManager(@RequestHeader("authorization") String authorization,
                                                     @RequestBody @RequestParam ReviewDto reviewDto) {
        Long clientId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(reviewService.addReview(clientId, reviewDto), HttpStatus.CREATED);
    }

}
