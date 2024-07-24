package com.sparta.studytrek.domain.study.repository;

import com.sparta.studytrek.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study,Long> {
}
