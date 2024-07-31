package com.sparta.studytrek.domain.answer.repository;


import static com.sparta.studytrek.domain.answer.entity.QAnswer.answer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.studytrek.domain.answer.entity.Answer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Answer> findByAllByOrderByCreatedAtDesc(Pageable pageable){
        List<Answer> answers = jpaQueryFactory
            .select(answer)
            .orderBy(answer.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalPage = jpaQueryFactory
            .select(answer.count())
            .from(answer)
            .fetchOne();

        return PageableExecutionUtils.getPage(answers, pageable, () -> totalPage);
    }

}
