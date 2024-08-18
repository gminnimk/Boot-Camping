package com.sparta.studytrek.domain.camp.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.domain.camp.service.CampService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camps/{id}/summary")
public class CampController {

    private final CampService campService;

    @GetMapping
    public ResponseEntity<ApiResponse> getSummary(@PathVariable Long id){
        String summary = campService.getSummary(id);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.SUMMARY_CREATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(summary)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
