package com.sparta.studytrek.domain.camp.scheduler;

import com.sparta.studytrek.domain.camp.service.CampService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CampRankingScheduler {

    private final CampService campService;

    /**
     * 매일 자정에 캠프의 순위를 업데이트하는 스케줄링 메서드
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateCampRankings() {
        log.info("캠프 순위 업데이트 시작");
        campService.updateCampRankingsBasedOnLikes();
        log.info("캠프 순위 업데이트 완료");
    }
}