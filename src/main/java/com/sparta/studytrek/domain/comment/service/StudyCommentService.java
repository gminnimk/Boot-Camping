package com.sparta.studytrek.domain.comment.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.dto.StudyCommentRequestDto;
import com.sparta.studytrek.domain.comment.dto.StudyCommentResponseDto;
import com.sparta.studytrek.domain.comment.entity.StudyComment;
import com.sparta.studytrek.domain.comment.repository.StudyCommentRepository;
import com.sparta.studytrek.domain.study.entity.Study;
import com.sparta.studytrek.domain.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyCommentService {

    private final StudyCommentRepository studyCommentRepository;
    private final StudyRepository studyRepository;

    /**
     * 댓글을 생성합니다.
     *
     * @param studyId    스터디 모집글 ID
     * @param requestDto 댓글 요청 데이터
     * @param user       댓글 작성자
     * @return 생성된 댓글 응답 데이터
     */
    @Transactional
    public StudyCommentResponseDto createComment(Long studyId, StudyCommentRequestDto requestDto,
        User user) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        StudyComment studyComment = new StudyComment(study, user, requestDto.getContent());
        StudyComment savedComment = studyCommentRepository.save(studyComment);
        return buildStudyCommentResponseDto(savedComment);
    }

    /**
     * 특정 스터디 모집글의 모든 댓글을 조회합니다.
     *
     * @param studyId 댓글을 조회할 스터디 모집글 ID
     * @return 댓글 응답 데이터 목록
     */
    @Transactional(readOnly = true)
    public List<StudyCommentResponseDto> getComments(Long studyId) {
        List<StudyComment> comments = studyCommentRepository.findByStudyId(studyId);
        return comments.stream().map(this::buildStudyCommentResponseDto)
            .toList();
    }

    /**
     * 특정 댓글을 조회합니다.
     *
     * @param studyId   스터디 모집글 ID
     * @param commentId 댓글 ID
     * @return 댓글 응답 데이터
     */
    @Transactional(readOnly = true)
    public StudyCommentResponseDto getComment(Long studyId, Long commentId) {
        StudyComment studyComment = studyCommentRepository.findByCommentIdAndStudyId(commentId, studyId);
        return buildStudyCommentResponseDto(studyComment);
    }

    /**
     * 댓글을 수정합니다.
     *
     * @param studyId    스터디 모집글 ID
     * @param commentId  댓글 ID
     * @param requestDto 수정 요청 데이터
     * @param user       댓글 작성자
     * @return 수정된 댓글 응답 데이터
     */
    @Transactional
    public StudyCommentResponseDto updateComment(Long studyId, Long commentId,
        StudyCommentRequestDto requestDto, User user) {
        StudyComment studyComment = studyCommentRepository.findByCommentIdAndStudyId(commentId, studyId);

        if (!studyComment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.COMMENT_UPDATE_NOT_AUTHORIZED);
        }

        studyComment.updateContent(requestDto.getContent());
        return buildStudyCommentResponseDto(studyComment);
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param studyId   스터디 모집글 ID
     * @param commentId 댓글 ID
     * @param user      댓글 작성자
     */
    @Transactional
    public void deleteComment(Long studyId, Long commentId, User user) {
        StudyComment studyComment = studyCommentRepository.findByCommentIdAndStudyId(commentId, studyId);

        if (!studyComment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.COMMENT_DELETE_NOT_AUTHORIZED);
        }

        studyCommentRepository.delete(studyComment);
    }

    /**
     * StudyComment 엔티티를 StudyCommentResponseDto로 변환합니다.
     *
     * @param studyComment 댓글 엔티티
     * @return 변환된 댓글 응답 데이터
     */
    private StudyCommentResponseDto buildStudyCommentResponseDto(StudyComment studyComment) {
        return StudyCommentResponseDto.builder().id(studyComment.getId())
            .content(studyComment.getContent()).createdAt(studyComment.getCreatedAt().toString())
            .modifiedAt(studyComment.getModifiedAt().toString()).build();
    }
}
