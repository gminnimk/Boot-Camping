//package com.sparta.studytrek.domain.rank.service;
//
//import com.sparta.studytrek.domain.rank.dto.RankResponseDto;
//import com.sparta.studytrek.domain.rank.dto.FilterRequest;
//import com.sparta.studytrek.domain.rank.entity.Rank;
//import com.sparta.studytrek.domain.rank.repository.RankRepository;
//import com.sparta.studytrek.domain.recruitment.dto.RecruitmentResponseDto;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//public class RankService {
//
//    private final RankRepository rankRepository;
//
//    public RankService(RankRepository rankRepository) {
//        this.rankRepository = rankRepository;
//    }
//
//    @Transactional(readOnly = true)
//    public List<RankResponseDto> getFilteredRanks(FilterRequest filterRequest) {
//        List<Rank> ranks = rankRepository.findFilteredRanks(
//            filterRequest.getTrack(),
//            filterRequest.getEnvironment(),
//            filterRequest.getCost()
//        );
//
//        return ranks.stream()
//            .map(rank -> {
//                RecruitmentResponseDto recruitmentDto = new RecruitmentResponseDto(
//                    rank.getRecruitment());
//                return new RankResponseDto(
//                    rank.getId(),
//                    rank.getRanking(),
//                    recruitmentDto
//                );
//            })
//            .toList();
//    }
//}
