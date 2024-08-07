package com.sparta.studytrek.domain.review.repository;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    default Review findByReviewId(Long id){
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_REVIEW));
    }
}
