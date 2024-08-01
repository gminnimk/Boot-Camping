package com.sparta.studytrek.domain.rank.repository;

import com.sparta.studytrek.domain.rank.entity.Rank;
import java.util.List;

public interface RankRepositoryCustom {
    List<Rank> findFilteredRanks(String track, String environment, String cost);
}
