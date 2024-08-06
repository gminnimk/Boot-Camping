package com.sparta.studytrek.domain.rank.repository;

import com.sparta.studytrek.domain.rank.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long>, RankRepositoryCustom {
}
