package com.sparta.studytrek.domain.camp.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.repository.UserRepository;
import com.sparta.studytrek.domain.camp.dto.CampRequestDto;
import com.sparta.studytrek.domain.camp.dto.CampResponseDto;
import com.sparta.studytrek.domain.camp.entity.Camp;
import com.sparta.studytrek.domain.camp.repository.CampRepository;
import com.sparta.studytrek.domain.like.entity.CampLike;
import com.sparta.studytrek.domain.like.repository.CampLikeRepository;
import com.sparta.studytrek.domain.rank.entity.Rank;
import com.sparta.studytrek.domain.rank.repository.RankRepository;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampService {

    private static final int DEFAULT_RANK_INCREMENT = 1;
    private final CampRepository campRepository;
    private final RankRepository rankRepository;
    private final UserRepository userRepository;
    private final CampLikeRepository campLikeRepository;

    public Camp findByName(String campName) {
        return campRepository.findByName(campName)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_CAMP));
    }

    public Camp findById(Long id) {
        return campRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_CAMP));
    }

    public CampResponseDto createCamp(CampRequestDto requestDto) {
        campRepository.findByName(requestDto.name()).ifPresent(
            camp -> {throw new CustomException(ErrorCode.DUPLICATE_CAMP_NAME);}
        );
        Camp camp = new Camp(requestDto.name(), requestDto.description(), requestDto.imageUrl());
        Camp savedCamp = campRepository.save(camp);

        Integer maxRanking = rankRepository.findMaxRanking().orElse(0);
        Integer newRanking = maxRanking + DEFAULT_RANK_INCREMENT;
        Rank rank = new Rank(savedCamp, newRanking);
        rankRepository.save(rank);
        return new CampResponseDto(savedCamp.getId(), savedCamp.getName(), savedCamp.getDescription(), savedCamp.getImageUrl());
    }

    public int likeCamp(Long campId, User userId) {
        Camp camp = findById(campId);
        User user = userRepository.findById(userId.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 이미 좋아요를 눌렀는지 확인
        if (campLikeRepository.existsByCampAndUser(camp, user)) {
            throw new CustomException(ErrorCode.ALREADY_LIKE);
        }

        CampLike campLike = new CampLike(camp, user);
        campLikeRepository.save(campLike);
        camp.incrementLikes();
        campRepository.save(camp);
        return 0;
    }

    public int unlikeCamp(Long campId, User userId) {
        Camp camp = findById(campId);
        User user = userRepository.findById(userId.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        CampLike campLike = campLikeRepository.findByCampAndUser(camp, user)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_LIKE));

        campLikeRepository.delete(campLike);
        camp.decrementLikes();
        campRepository.save(camp);
        return 0;
    }

    /**
     * 좋아요 수에 기반하여 캠프의 순위를 업데이트하는 메서드
     */
    @Transactional
    public void updateCampRankingsBasedOnLikes() {
        List<Camp> camps = campRepository.findAll();

        // 좋아요 수에 따라 캠프를 정렬
        camps.sort(Comparator.comparingInt(Camp::getLikesCount).reversed());

        // 순위를 매기고 Rank 엔티티 업데이트
        int rank = 1;
        for (Camp camp : camps) {
            Rank campRank = rankRepository.findByCamp(camp)
                .orElse(new Rank(camp, rank));
            campRank.setRanking(rank);
            rankRepository.save(campRank);
            rank++;
        }
    }
}