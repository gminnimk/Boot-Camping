package com.sparta.studytrek.domain.recruitment.repository;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentRepositoryCustom {
    Page<Recruitment> findAllByOrderByCreatedAtDesc(Pageable pageable);

    default Recruitment findByRecruitmentId(Long id){
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_RECRUITMENT));
    }
}
