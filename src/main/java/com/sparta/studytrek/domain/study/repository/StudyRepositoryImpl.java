package com.sparta.studytrek.domain.study.repository;

import static com.sparta.studytrek.domain.study.entity.QStudy.study;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.studytrek.domain.study.entity.Study;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Study> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        List<Study> studies = jpaQueryFactory
            .selectFrom(study)
            .orderBy(study.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCount = jpaQueryFactory
            .select(study.count())
            .from(study)
            .fetchOne();

        return PageableExecutionUtils.getPage(studies, pageable, () -> totalCount);
    }
}
