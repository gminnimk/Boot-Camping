package com.sparta.studytrek.domain.recruitment.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentRequestDto;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentResponseDto;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import com.sparta.studytrek.domain.recruitment.repository.RecruitmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    /**
     * 모집글 작성
     *
     * @param requestDto 모집글 등록 요청 데이터
     * @param user       요청한 유저의 정보
     * @return 모집글 응답 데이터
     */
    @Transactional
    public RecruitmentResponseDto createRecruitment(RecruitmentRequestDto requestDto, User user) {
        Recruitment recruitment = new Recruitment(requestDto, user);
        Recruitment createRecruitment = recruitmentRepository.save(recruitment);
        return new RecruitmentResponseDto(createRecruitment);
    }

    /**
     * 모집글 수정
     *
     * @param id         모집글 ID
     * @param requestDto 모집글 등록 요청 데이터
     * @param user       요청한 유저의 정보
     * @return 모집글 응답 데이터
     */
    @Transactional
    public RecruitmentResponseDto updateRecruitment(Long id, RecruitmentRequestDto requestDto,
        User user) {
        Recruitment recruitment = findByRecruitmentId(id);
        reqUserCheck(recruitment.getUser().getId(), user.getId());

        recruitment.updateRecruitment(requestDto);
        return new RecruitmentResponseDto(recruitment);
    }

    /**
     * 모집글 삭제
     *
     * @param id   모집글 ID
     * @param user 요청한 유저의 정보
     */
    public void deleteRecruitment(Long id, User user) {
        Recruitment recruitment = findByRecruitmentId(id);
        reqUserCheck(recruitment.getUser().getId(), user.getId());
        recruitmentRepository.delete(recruitment);
    }

    /**
     * 모집글 전체 조회
     *
     * @param pageable 페이지 정보
     * @return 모집글 전체 목록
     */
    public Page<RecruitmentResponseDto> getAllRecruitments(Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findAllByOrderByCreatedAtDesc(pageable);
        return recruitments.map(RecruitmentResponseDto::new);
    }

    /**
     * 모집글 단건 조회
     *
     * @param id 모집글 ID
     * @return 해당 모집글의 응답 데이터
     */
    public RecruitmentResponseDto getRecruitment(Long id) {
        Recruitment recruitment = findByRecruitmentId(id);
        return new RecruitmentResponseDto(recruitment);
    }

    /**
     * 모집글 찾기
     *
     * @param id 모집글 ID
     * @return 해당 모집글의 정보
     */
    public Recruitment findByRecruitmentId(Long id) {
        return recruitmentRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_RECRUITMENT));
    }

    /**
     * 모집글을 작성한 유저와 해당 기능을 요청한 유저가 동일한지
     *
     * @param recruitmentUserId 모집글을 작성한 유저 ID
     * @param userId            유저 ID
     */
    public void reqUserCheck(Long recruitmentUserId, Long userId) {
        if (!recruitmentUserId.equals(userId)) {
            throw new CustomException(ErrorCode.RECRUITMENT_NOT_AUTHORIZED);
        }
    }
}