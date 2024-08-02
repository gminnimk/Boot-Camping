package com.sparta.studytrek.domain.recruitment.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.domain.recruitment.dto.ParticipatePeriodResponseDto;
import com.sparta.studytrek.domain.recruitment.dto.RecruitPeriodResponseDto;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import com.sparta.studytrek.domain.recruitment.service.PeriodService;
import com.sparta.studytrek.domain.recruitment.service.RecruitmentService;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class PeriodController {

    private final PeriodService periodService;

    /**
     * 부트캠프 모집 기간 조회
     *
     * @return 해당 부트캠프의 모집 기간 데이터
     */
    @GetMapping("/recruit")
    public ResponseEntity<ApiResponse> getRecruitments()
    {
        List<RecruitPeriodResponseDto> responseDto = periodService.getRecruitments();
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.GET_RECRUITMENT_PERIOD.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 부트캠프 참여 기간 조회
     *
     * @return 해당 부트캠프의 참여 기간 데이터
     */
    @GetMapping("/participate")
    public ResponseEntity<ApiResponse> getParticipates()
    {
        List<ParticipatePeriodResponseDto> responseDto = periodService.getParticipates();
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.GET_PARTICIPATE_PERIOD.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
