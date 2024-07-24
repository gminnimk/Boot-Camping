package com.sparta.studytrek.domain.camp.repository;

import com.sparta.studytrek.domain.camp.entity.Camp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampRepository extends JpaRepository<Camp, Long> {
    Optional<Camp> findByName(String name);
}
