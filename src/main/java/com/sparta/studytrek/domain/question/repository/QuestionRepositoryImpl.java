package com.sparta.studytrek.domain.question.repository;

import static com.sparta.studytrek.domain.question.entity.QQuestion.question;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.studytrek.domain.question.entity.Question;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Question> findByAllByOrderByCreatedAtDesc(Pageable pageable){
        List<Question> questions = jpaQueryFactory
            .selectFrom(question)
            .orderBy(question.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalPage = jpaQueryFactory
            .select(question.count())
            .from(question)
            .fetchOne();

        return PageableExecutionUtils.getPage(questions, pageable, () -> totalPage);
    }

}
