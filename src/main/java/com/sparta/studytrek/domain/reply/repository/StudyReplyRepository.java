package com.sparta.studytrek.domain.reply.repository;

import com.sparta.studytrek.domain.reply.entity.StudyReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyReplyRepository extends JpaRepository<StudyReply, Long> {

}
