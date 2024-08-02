//package com.sparta.studytrek.domain.rank.service;
//
//import com.sparta.studytrek.domain.camp.dto.CampResponseDto;
//import com.sparta.studytrek.domain.rank.dto.RankResponseDto;
//import com.sparta.studytrek.domain.rank.dto.FilterRequest;
//import com.sparta.studytrek.domain.rank.entity.Rank;
//import com.sparta.studytrek.domain.rank.repository.RankRepository;
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
//    // 필터 조건에 맞는 랭킹 데이터를 조회하고, 이를 클라이언트에 반환할 수 있는 형태로 변환
//    @Transactional(readOnly = true)
//    public List<RankResponseDto> getFilteredRanks(FilterRequest filterRequest) {
//        List<Rank> ranks = rankRepository.findFilteredRanks(
//            filterRequest.getTrack(),
//            filterRequest.getEnvironment(),
//            filterRequest.getCost()
//        );
//
//        return ranks.stream()
//            .map(rank -> new RankResponseDto(
//                rank.getId(),
//                new CampResponseDto(
//                    rank.getCamp().getId(),
//                    rank.getCamp().getName(),
//                    rank.getCamp().getDescription(),
//                    rank.getCamp().getTrack(),
//                    rank.getCamp().getEnvironment(),
//                    rank.getCamp().getCost()
//                ),
//                rank.getRanking()
//            ))
//            .toList();
//    }
//}