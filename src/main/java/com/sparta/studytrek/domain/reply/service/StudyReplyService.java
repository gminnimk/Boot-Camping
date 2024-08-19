package com.sparta.studytrek.domain.reply.service;

import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.entity.StudyComment;
import com.sparta.studytrek.domain.comment.repository.StudyCommentRepository;
import com.sparta.studytrek.domain.reply.dto.StudyReplyRequestDto;
import com.sparta.studytrek.domain.reply.dto.StudyReplyResponseDto;
import com.sparta.studytrek.domain.reply.entity.StudyReply;
import com.sparta.studytrek.domain.reply.repository.StudyReplyRepository;
import com.sparta.studytrek.domain.study.entity.Study;
import com.sparta.studytrek.domain.study.repository.StudyRepository;
import com.sparta.studytrek.notification.service.NotificationService;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyReplyService {

    private final StudyReplyRepository studyReplyRepository;
    private final StudyRepository studyRepository;
    private final StudyCommentRepository studyCommentRepository;
    private final NotificationService notificationService;

    /**
     * 대댓글을 생성합니다.
     *
     * @param studyId    대댓글을 작성할 스터디 모집글의 ID
     * @param commentId  대댓글을 작성할 댓글의 ID
     * @param requestDto 대댓글 작성 데이터
     * @param user       대댓글 작성자 정보
     * @return 생성된 대댓글 응답 데이터
     */
    @Transactional
    public StudyReplyResponseDto createReply(Long studyId, Long commentId,
        StudyReplyRequestDto requestDto, User user) throws IOException {
        Study study = studyRepository.findByStudyId(studyId);

        StudyComment studyComment = studyCommentRepository.findByCommentId(commentId);

        StudyReply studyReply = new StudyReply(study, studyComment, user, requestDto.getContent());
        StudyReply savedReply = studyReplyRepository.save(studyReply);

        notificationService.createAndSendNotification(
            studyComment.getUser().getUsername(),
            ResponseText.NOTIFICATION_STUDY_REPLY_CREATED.getMsg()
        );

        return buildStudyReplyResponseDto(savedReply);
    }

    /**
     * 특정 댓글의 모든 대댓글을 조회합니다.
     *
     * @param studyId   대댓글을 조회할 스터디 모집글의 ID
     * @param commentId 대댓글을 조회할 댓글의 ID
     * @return 대댓글 응답 데이터 목록
     */
    @Transactional(readOnly = true)
    public List<StudyReplyResponseDto> getReplies(Long studyId, Long commentId) {
        List<StudyReply> replies = studyReplyRepository.findByStudyIdAndStudyCommentId(studyId,
            commentId);
        return replies.stream()
            .map(this::buildStudyReplyResponseDto)
            .toList();
    }

    /**
     * 특정 대댓글을 조회합니다.
     *
     * @param commentId 대댓글을 조회할 댓글의 ID
     * @param replyId   조회할 대댓글의 ID
     * @return 특정 대댓글 응답 데이터
     */
    @Transactional(readOnly = true)
    public StudyReplyResponseDto getReply(Long commentId, Long replyId) {
        StudyReply studyReply = studyReplyRepository.findByReplyIdAndStudyCommentId(replyId, commentId);
        return buildStudyReplyResponseDto(studyReply);
    }

    /**
     * 대댓글을 수정합니다.
     *
     * @param commentId  대댓글을 수정할 댓글의 ID
     * @param replyId    수정할 대댓글의 ID
     * @param requestDto 수정 요청 데이터
     * @param user       대댓글 작성자 정보
     * @return 수정된 대댓글 응답 데이터
     */
    @Transactional
    public StudyReplyResponseDto updateReply(Long commentId, Long replyId,
        StudyReplyRequestDto requestDto, User user) {
        StudyReply studyReply = studyReplyRepository.findByReplyIdAndStudyCommentId(replyId, commentId);

        if (!studyReply.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.REPLY_UPDATE_NOT_AUTHORIZED);
        }

        studyReply.updateContent(requestDto.getContent());
        return buildStudyReplyResponseDto(studyReply);
    }

    /**
     * 대댓글을 삭제합니다.
     *
     * @param commentId 대댓글을 삭제할 댓글의 ID
     * @param replyId   삭제할 대댓글의 ID
     * @param user      대댓글 작성자 정보
     */
    @Transactional
    public void deleteReply(Long commentId, Long replyId, User user) {
        StudyReply studyReply = studyReplyRepository.findByReplyIdAndStudyCommentId(replyId, commentId);

        if (!studyReply.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.REPLY_DELETE_NOT_AUTHORIZED);
        }

        studyReplyRepository.delete(studyReply);
    }

    /**
     * StudyReply 엔티티를 StudyReplyResponseDto로 변환합니다.
     *
     * @param studyReply 대댓글 엔티티
     * @return 변환된 대댓글 응답 데이터
     */
    private StudyReplyResponseDto buildStudyReplyResponseDto(StudyReply studyReply) {
        return StudyReplyResponseDto.builder()
            .id(studyReply.getId())
            .content(studyReply.getContent())
            .createdAt(studyReply.getCreatedAt().toString())
            .modifiedAt(studyReply.getModifiedAt().toString())
            .build();
    }
}
