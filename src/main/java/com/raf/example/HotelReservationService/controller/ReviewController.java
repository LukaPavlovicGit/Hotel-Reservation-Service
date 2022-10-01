package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.domain.Review;
import com.raf.example.HotelReservationService.dto.ReviewDto;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    @ApiOperation(value = "make a comment")
    public ResponseEntity<ReviewDto> addReview(@RequestHeader("authorization") String authorization,
                                               @RequestBody ReviewDto reviewDto) {
        Long clientId = securityAspect.getUserId(authorization);
        reviewDto.setClientId(clientId);
        return new ResponseEntity<>(reviewService.save(reviewDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @ApiOperation("list reviews")
    public ResponseEntity<List<ReviewDto>> getAllReviews(@RequestParam(required = false, value = "hotelName") String hotelName,
                                                         @RequestParam(required = false, value = "city") String city){
        return new ResponseEntity<>(reviewService.getReviews(hotelName,city), HttpStatus.OK);
    }

    @DeleteMapping
    @CheckSecurity(roles = {"ROLE_CLIENT", "ROLE_MANAGER"})
    @ApiOperation(value = "remove comment")
    public ResponseEntity<ReviewDto> removeReview(@RequestHeader("authorization") String authorization,
                                                  @RequestParam(required = false, value = "reviewId") Long reviewId) {
        Long clientId = securityAspect.getUserId(authorization);
        String role = securityAspect.getUserRole(authorization);
        return new ResponseEntity<>(reviewService.remove(reviewId, role, clientId), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    @ApiOperation(value = "update comment")
    public ResponseEntity<ReviewDto> updateReview(@RequestHeader("authorization") String authorization,
                                                  @PathVariable Long id, @RequestBody @Valid ReviewDto reviewDto) {
        Long clientId = securityAspect.getUserId(authorization);
        reviewDto.setClientId(clientId);
        return new ResponseEntity<>(reviewService.update(id, reviewDto), HttpStatus.OK);
    }
}
