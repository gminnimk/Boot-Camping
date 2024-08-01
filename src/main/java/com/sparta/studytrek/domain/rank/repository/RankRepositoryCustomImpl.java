package com.sparta.studytrek.domain.rank.repository;

import com.querydsl.core.BooleanBuilder;
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
    public List<Rank> findFilteredRanks(List<String> tracks, List<String> environments, List<String> costs) {
        QRank rank = QRank.rank;
        QCamp camp = QCamp.camp;

        BooleanBuilder builder = new BooleanBuilder();

        if (tracks != null && !tracks.isEmpty()) {
            builder.and(camp.track.in(tracks));
        }
        if (environments != null && !environments.isEmpty()) {
            builder.and(camp.environment.in(environments));
        }
        if (costs != null && !costs.isEmpty()) {
            builder.and(camp.cost.in(costs));
        }

        return jpaQueryFactory
            .selectFrom(rank)
            .join(rank.camp, camp)
            .where(builder)
            .fetch();
    }
}

