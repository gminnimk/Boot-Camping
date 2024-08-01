package com.sparta.studytrek.domain.rank.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.studytrek.domain.rank.entity.QRank;
import com.sparta.studytrek.domain.camp.entity.QCamp;
import com.sparta.studytrek.domain.rank.entity.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankRepositoryCustomImpl implements RankRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Rank> findFilteredRanks(String track, String environment, String cost) {
        QRank rank = QRank.rank;
        QCamp camp = QCamp.camp;

        return jpaQueryFactory
            .selectFrom(rank)
            .join(rank.camp, camp)
            .where(
                (track != null ? camp.track.eq(track) : null),
                (environment != null ? camp.environment.eq(environment) : null),
                (cost != null ? camp.cost.eq(cost) : null)
            )
            .fetch();
    }
}
