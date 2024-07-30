package com.sparta.studytrek.domain.review.repository;

import static com.sparta.studytrek.domain.review.entity.QReview.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.studytrek.domain.review.entity.Review;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        List<Review> reviews = jpaQueryFactory
            .selectFrom(review)
            .orderBy(review.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalPage = jpaQueryFactory
            .select(review.count())
            .from(review)
            .fetchOne();

        return PageableExecutionUtils.getPage(reviews, pageable, () -> totalPage);
    }
}
