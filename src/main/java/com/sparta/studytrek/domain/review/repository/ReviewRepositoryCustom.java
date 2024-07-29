package com.sparta.studytrek.domain.review.repository;

import com.sparta.studytrek.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
