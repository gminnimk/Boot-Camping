package com.sparta.studytrek.domain.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sparta.studytrek.domain.auth.entity.Role;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.domain.auth.entity.UserType;
import com.sparta.studytrek.domain.auth.repository.RoleRepository;
import com.sparta.studytrek.domain.auth.repository.UserRepository;
import com.sparta.studytrek.domain.camp.entity.Camp;
import com.sparta.studytrek.domain.camp.repository.CampRepository;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentRequestDto;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentResponseDto;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import com.sparta.studytrek.domain.recruitment.repository.RecruitmentRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecruitmentServiceTest {

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private CampRepository campRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RecruitmentService recruitmentService;

    Role role;
    UserType userType;

    @BeforeEach
    void setUp() {
        UserRoleEnum roleEnum = UserRoleEnum.USER;
        role = new Role(roleEnum);
        userType = UserType.NORMAL;
        roleRepository.save(role);
    }

    @Test
    @Transactional
    void getRecruitmentSummary() {
        // given
        String updateSummary = "update Recruitment Summary";
        User user = new User("test22", "test1234!", "name", "addr", userType, role);
        Camp camp = new Camp("카카오 캠프","카카오에서 진행하는 캠프","img.png");
        Recruitment recruitment = new Recruitment(
            RecruitmentRequestDto.builder()
                .title("title")
                .process("process")
                .content("content")
                .place("place")
                .cost("cost")
                .trek("trek")
                .level("level")
                .classTime("classTime")
                .campStart(LocalDate.parse("2024-08-19"))
                .campEnd(LocalDate.parse("2024-08-20"))
                .recruitStart(LocalDate.parse("2024-07-17"))
                .recruitEnd(LocalDate.parse("2024-07-18"))
                .campName("카카오 캠프")
                .imageUrl("img.png")
                .build(),
            user, camp);
        userRepository.save(user);
        campRepository.save(camp);
        recruitmentRepository.save(recruitment);

        camp.updateSummary(updateSummary);

        // when
        RecruitmentResponseDto responseDto = recruitmentService.getRecruitment(recruitment.getId());

        // then
        assertEquals(updateSummary, responseDto.getSummary());
    }
}