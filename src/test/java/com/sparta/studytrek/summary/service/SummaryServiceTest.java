package com.sparta.studytrek.summary.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SummaryServiceTest {

    @Autowired
    private SummaryService summaryService;

    @Test
    void summarizeText() {
        // given - 요약 대상 리뷰 메시지 목록
        var text = List.of("강의가 잘 구성되어 있어서 이해하기 쉬웠습니다. 기본 개념부터 심화 내용까지 체계적으로 설명해 주셔서 학습에 큰 도움이 되었습니다. 또한, 실제 사례를 통해 학습 내용을 적용해 볼 수 있는 기회를 제공해 주셔서 더욱 유익했습니다.",
            "커리큘럼이 체계적이고 실습 중심으로 잘 짜여있습니다. 각 주제별로 필요한 실습 과제가 포함되어 있어 실무에 필요한 능력을 키울 수 있었습니다. 또한, 이론과 실습의 적절한 균형이 학습의 효율성을 높였습니다.",
            "강의 자료가 매우 유용하고 잘 정리되어 있습니다. 각 챕터마다 핵심 내용이 요약되어 있어 복습할 때 큰 도움이 되었습니다. 특히 실습 예제와 관련된 추가 자료가 제공되어 실습을 더 깊이 있게 이해할 수 있었습니다.",
            "강의 내용이 흥미롭고 유익했습니다. 다양한 사례와 실습을 통해 복잡한 개념도 쉽게 이해할 수 있었으며, 수업이 지루하지 않게 진행되어 학습에 대한 동기 부여도 되었습니다. 이 강의를 통해 많은 것을 배울 수 있었습니다.",
            "프로젝트가 실제 업무와 유사하여 많은 도움이 되었습니다. 프로젝트를 통해 실무에서의 문제 해결 능력을 키울 수 있었고, 팀원들과 협업하는 과정에서 소통 능력도 향상되었습니다. 실제 프로젝트와 유사한 경험을 제공해 주셔서 감사합니다.",
            "프로젝트의 퀄리티가 높아서 매우 만족스러웠습니다. 실무에서 바로 적용 가능한 기술을 사용한 프로젝트였으며, 팀원들과의 협업을 통해 많은 것을 배울 수 있었습니다. 이 프로젝트를 통해 실전 경험을 쌓을 수 있었습니다.");
        var language = "ko";
        var summaryCount = 3;

        // when - 요약 API 호출
        var summarizedText = summaryService.summarizeText(text, language, summaryCount);

        // then - 요약 결과 응답
        System.out.println(summarizedText);
        assertNotNull(summarizedText);
    }
}