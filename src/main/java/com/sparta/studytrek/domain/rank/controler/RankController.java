//package com.sparta.studytrek.domain.rank.controler;
//
//import com.sparta.studytrek.domain.rank.dto.RankResponseDto;
//import com.sparta.studytrek.domain.rank.dto.FilterRequest;
//import com.sparta.studytrek.domain.rank.service.RankService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/ranks")
//public class RankController {
//
//    @Autowired
//    private RankService rankService;
//
//    @GetMapping("/filter")
//    public List<RankResponseDto> filterRanks(
//        @RequestParam(required = false) List<String> track,
//        @RequestParam(required = false) List<String> environment,
//        @RequestParam(required = false) List<String> cost) {
//
//        FilterRequest filterRequest = new FilterRequest(track, environment, cost);
//        return rankService.getFilteredRanks(filterRequest);
//    }
//}