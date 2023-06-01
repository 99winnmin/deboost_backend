package com.samnamja.deboost.api.service.riot;

import com.samnamja.deboost.api.dto.openfeign.request.FlaskRequestDto;
import com.samnamja.deboost.api.dto.openfeign.response.FlaskResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlaskService {
    private final RestTemplate restTemplate;


    public FlaskResponseDto analysisGameData(FlaskRequestDto flaskRequestDto) {
        // Request Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Request Body 설정
        HttpEntity<FlaskRequestDto> request = new HttpEntity<>(flaskRequestDto, headers);

        ResponseEntity<FlaskResponseDto> response = restTemplate.exchange(
                "/analysis",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<FlaskResponseDto>() {}
        );
        return response.getBody();
    }
}
