package com.sparta.studytrek.domain.rank.controler;

import com.sparta.studytrek.domain.rank.dto.RankResponseDto;
import com.sparta.studytrek.domain.rank.dto.FilterRequest;
import com.sparta.studytrek.domain.rank.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ranks")
public class RankController {

    @Autowired
    private RankService rankService;

    @GetMapping("/filter")
    public List<RankResponseDto> filterRanks(
        @RequestParam(required = false) String track,
        @RequestParam(required = false) String environment,
        @RequestParam(required = false) String cost) {

        // filterRanks: 다양한 필터 조건 (track, environment, cost)을 받아 필터링된 랭킹 목록을 반환합니다.
        FilterRequest filterRequest = new FilterRequest(track, environment, cost);
        return rankService.getFilteredRanks(filterRequest);
    }
}