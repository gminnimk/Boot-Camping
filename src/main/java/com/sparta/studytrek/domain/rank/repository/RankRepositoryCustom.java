package com.sparta.studytrek.domain.rank.repository;

import com.sparta.studytrek.domain.rank.entity.Rank;
import java.util.List;

public interface RankRepositoryCustom {
    List<Rank> findFilteredRanks(List<String> tracks, List<String> environments, List<String> costs);
}
