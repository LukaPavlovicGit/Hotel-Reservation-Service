package com.raf.example.HotelReservationService.clientUserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;

@Configuration
public class UserServiceClientConfiguration {
    @Bean
    public RestTemplate userServiceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8080/api"));
        return restTemplate;
    }

    /*private class TokenInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJpZCI6NCwicm9sZSI6IlJPTEVfTUFOQUdFUiIsImVtYWlsIjoibTFAZ21haWwuY29tIn0.AN5J7PRlA3uLe0srUJ8WQjcZD-MGdIT0bRd5vVh49t-IMRoC3NYDcbf_lFIgFXjX-mYe8SIqV32-AgreD5FiWQ");
            return execution.execute(request, body);
        }
    }*/
}