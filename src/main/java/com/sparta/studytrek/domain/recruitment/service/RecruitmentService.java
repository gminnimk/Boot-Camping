package com.sparta.studytrek.domain.recruitment.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.config.aws.S3Uploader;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentRequestDto;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentResponseDto;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import com.sparta.studytrek.domain.recruitment.repository.RecruitmentRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final S3Uploader s3Uploader;

    /**
     * 모집글 작성
     *
     * @param requestDto 모집글 등록 요청 데이터
     * @param imageFile  이미지 파일
     * @param user       요청한 유저의 정보
     * @return 모집글 응답 데이터
     */
    @Transactional
    public RecruitmentResponseDto createRecruitment(RecruitmentRequestDto requestDto,
        MultipartFile imageFile, User user)
        throws IOException {
        // 이미지 파일 검증 및 업로드
        String imageUrl = validateAndUploadImage(imageFile);
        requestDto.setImageUrl(imageUrl);

        Recruitment recruitment = new Recruitment(requestDto, user);
        Recruitment createRecruitment = recruitmentRepository.save(recruitment);
        return new RecruitmentResponseDto(createRecruitment);
    }

    /**
     * 모집글 수정
     *
     * @param id         모집글 ID
     * @param requestDto 모집글 등록 요청 데이터
     * @param imageFile  이미지 파일
     * @param user       요청한 유저의 정보
     * @return 모집글 응답 데이터
     */
    @Transactional
    public RecruitmentResponseDto updateRecruitment(Long id, RecruitmentRequestDto requestDto,
        MultipartFile imageFile, User user) throws IOException {
        Recruitment recruitment = recruitmentRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_RECRUITMENT));

        recruitment.updateRecruitmentWithoutImgurl(requestDto);
        if (imageFile != null && !imageFile.isEmpty()) {
            String newImageUrl = s3Uploader.upload(imageFile, "recruitment-images");
            String oldImageUrl = recruitment.getImageUrl();
            if (oldImageUrl != null) {
                s3Uploader.delete(oldImageUrl);
            }
            recruitment.updateImage(newImageUrl);
        }
        recruitmentRepository.save(recruitment);
        return new RecruitmentResponseDto(recruitment);
    }

    /**
     * 이미지 파일 검증 및 업로드
     *
     * @param imageFile 업로드할 이미지 파일
     * @return 업로드된 이미지의 URL
     * @throws IllegalArgumentException 이미지 파일이 없거나 빈 경우
     */
    private String validateAndUploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_TYPE_REQUIRED);
        }
        return s3Uploader.upload(imageFile, "recruitment-images");
    }

    /**
     * 모집글 삭제
     *
     * @param id   모집글 ID
     * @param user 요청한 유저의 정보
     */
    public void deleteRecruitment(Long id, User user) {
        Recruitment recruitment = recruitmentRepository.findByRecruitmentId(id);
        reqUserCheck(recruitment.getUser().getId(), user.getId());
        s3Uploader.delete(recruitment.getImageUrl());
        recruitmentRepository.delete(recruitment);
    }

    /**
     * 모집글 전체 조회
     *
     * @param pageable 페이지 정보
     * @return 모집글 전체 목록
     */
    public Page<RecruitmentResponseDto> getAllRecruitments(Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findAllByOrderByCreatedAtDesc(
            pageable);
        return recruitments.map(RecruitmentResponseDto::new);
    }

    /**
     * 모집글 단건 조회
     *
     * @param id 모집글 ID
     * @return 해당 모집글의 응답 데이터
     */
    public RecruitmentResponseDto getRecruitment(Long id) {
        Recruitment recruitment = recruitmentRepository.findByRecruitmentId(id);
        return new RecruitmentResponseDto(recruitment);
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

    /**
     * 필터링된 모집 공고를 가져옵니다.
     *
     * @param treks  모집 공고의 트랙 유형 리스트. 필터링 기준으로 사용
     * @param places 모집 공고의 장소 리스트. 필터링 기준으로 사용
     * @param costs  모집 공고의 비용 리스트. 필터링 기준으로 사용
     * @return 주어진 필터 조건에 맞는 모집 공고를 담은 `RecruitmentResponseDto` 리스트.
     */
    public List<RecruitmentResponseDto> getFilteredRecruitments(List<String> treks,
        List<String> places, List<String> costs) {
        List<Recruitment> recruitments = recruitmentRepository.filterRecruitments(treks, places,
            costs);
        return recruitments.stream()
            .map(RecruitmentResponseDto::new)
            .toList();
    }
}
