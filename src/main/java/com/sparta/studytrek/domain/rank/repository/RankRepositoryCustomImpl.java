//package com.sparta.studytrek.domain.rank.repository;
//
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.sparta.studytrek.domain.rank.entity.QRank;
//import com.sparta.studytrek.domain.recruitment.entity.QRecruitment;
//import com.sparta.studytrek.domain.rank.entity.Rank;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class RankRepositoryCustomImpl implements RankRepositoryCustom {
//
//    @Autowired
//    private JPAQueryFactory jpaQueryFactory;
//
//    @Override
//    public List<Rank> findFilteredRanks(List<String> tracks, List<String> environments,
//        List<String> costs) {
//        QRank rank = QRank.rank;
//        QRecruitment recruitment = QRecruitment.recruitment;
//
//        BooleanBuilder builder = new BooleanBuilder();
//
//        // 동적 쿼리 같은 거는 enum 으로 넘어오는 경우에
//        // 사용 하지만 지금은 null인 경우일 때
//        if (tracks != null && !tracks.isEmpty()) {
//            builder.and(recruitment.trek.in(tracks));
//        }
//        if (environments != null && !environments.isEmpty()) {
//            builder.and(recruitment.place.in(environments));
//        }
//        if (costs != null && !costs.isEmpty()) {
//            builder.and(recruitment.cost.in(costs));
//        }
//
//        return jpaQueryFactory
//            .selectFrom(rank)
//            .join(rank.recruitment, recruitment)
//            .where(builder)
//            .fetch();
//    }
//}