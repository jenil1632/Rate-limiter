package com.example.ratelimiter.controller;

import com.example.ratelimiter.service.RateLimitingService;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

  private RateLimitingService rateLimitingService = RateLimitingService.getInstance();

  @GetMapping("/test/{userId}")
  public ResponseEntity<String> getMessage(@PathVariable String userId) {
    ResponseEntity<String> response;
    if (rateLimitingService.isRequestRateLimited(userId, LocalDateTime.now())) {
      response = new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
      return response;
    }
    response = new ResponseEntity<>("Hello!", HttpStatus.OK);
    return response;
  }

}
