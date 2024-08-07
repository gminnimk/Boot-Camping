//package com.sparta.studytrek.domain.rank.entity;
//
//import com.sparta.studytrek.domain.camp.entity.Camp;
//import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "rankings")
//@Getter
//@NoArgsConstructor
//public class Rank {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "recruitment_id", nullable = false)
//    private Recruitment recruitment;
//
//    @ManyToOne
//    @JoinColumn(name = "camp_id", nullable = false)
//    private Camp camp;
//
//    @Column(nullable = false)
//    private Integer ranking;
//
//    public Rank(Recruitment recruitment, Camp camp, Integer ranking) {
//        this.recruitment = recruitment;
//        this.camp = camp;
//        this.ranking = ranking;
//    }
//}
