document.addEventListener('DOMContentLoaded', function () {

  const pathSegments = window.location.pathname.split('/');
  currentStudyId = pathSegments[pathSegments.length - 1]; // URL에서 스터디 ID 추출

  if (currentStudyId) {
    fetchStudyDetails(currentStudyId);
    fetchComments(currentStudyId); // 댓글을 가져오는 함수 호출
  } else {
    console.error('스터디 ID가 제공되지 않았습니다.');
  }

  // 수정 버튼 클릭 시 수정 페이지로 이동
  document.querySelector('.modify-button').addEventListener('click',
      function () {
        if (currentStudyId) {
          window.location.href = `/study/modify?studyId=${currentStudyId}`;
        } else {
          Swal.fire({
            title: '오류',
            text: '스터디 ID를 찾을 수 없습니다.',
            icon: 'error',
            confirmButtonText: '확인'
          });
        }
      });

});

// 스터디 상세 정보를 가져오는 함수
function fetchStudyDetails(id) {
  fetch(`/api/studies/${id}`)  // 백틱(`)을 사용하여 템플릿 리터럴로 수정
  .then(response => response.json())
  .then(data => {
    if (data.statuscode === "200") {
      updateStudyDetails(data.data);
    } else {
      console.error('에러:', data.msg);
    }
  })
  .catch(error => console.error('API 호출 중 에러 발생:', error));
}

// 스터디 상세 정보를 화면에 표시하는 함수
function updateStudyDetails(study) {

  // 제목 설정
  document.querySelector('#title').textContent = study.title || '';

  // 현재 날짜 설정
  document.querySelector(
      '#currentDate').textContent = new Date().toLocaleDateString();

  // 카테고리 설정
  document.querySelector('#category').textContent = study.category || '';

  // 설명 설정
  document.querySelector('#description').textContent = study.content || '';

  // 최대 인원 설정
  document.querySelector('#maxMembers').textContent = study.maxCount || '';

  // 예상 기간 설정
  document.querySelector('#duration').textContent = study.periodExpected || '';

  // 모임 주기 설정
  document.querySelector('#meetingFrequency').textContent = study.cycle || '';
}

// 스터디 삭제 함수
function deleteStudy() {
  Swal.fire({
    title: '스터디 삭제',
    text: '정말로 이 스터디를 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '삭제',
    cancelButtonText: '취소'
  }).then((result) => {
    if (result.isConfirmed) {
      fetch(`/api/studies/${currentStudyId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        }
      })
      .then(response => response.json())
      .then(data => {
        if (data.statuscode === "204") {
          Swal.fire({
            title: '삭제 완료',
            text: '스터디가 성공적으로 삭제되었습니다.',
            icon: 'success',
            confirmButtonText: '확인'
          }).then(() => {
            window.location.href = '/study/main'; // StudyMain.html로 리다이렉트
          });
        } else {
          throw new Error(data.msg || '스터디 삭제 중 오류가 발생했습니다.');
        }
      })
      .catch(error => {
        console.error('API 호출 중 에러 발생:', error);
        Swal.fire({
          title: '삭제 실패',
          text: error.message,
          icon: 'error',
          confirmButtonText: '확인'
        });
      });
    }
  });
}

// 댓글을 가져오는 함수
function fetchComments(studyId) {
  fetch(`/api/studies/${studyId}/comments`)
  .then(response => response.json())
  .then(data => {
    if (data.statuscode === "200") {
      displayComments(data.data);
    } else {
      console.error('댓글 조회 에러:', data.msg);
    }
  })
  .catch(error => console.error('API 호출 중 에러 발생:', error));
}

// 댓글을 화면에 표시하는 함수
function displayComments(comments) {
  const commentsSection = document.querySelector('.comments-section');
  commentsSection.innerHTML = ''; // 기존 댓글 제거

  comments.forEach(comment => {
    const commentElement = document.createElement('div');
    commentElement.className = 'comment';
    commentElement.id = `comment${comment.id}`;
    commentElement.innerHTML = `
            <div class="comment-header">
                <div class="comment-author">${comment.author || '익명 사용자'}</div>
                <div class="comment-date">${new Date(
        comment.createdAt).toISOString().split('T')[0]}</div>
            </div>
            <div class="comment-content">${comment.content}</div>
            <div class="comment-actions">
                <button class="reply-button" onclick="showReplyForm('${comment.id}')">답글</button>
                <button class="edit-button" onclick="editComment('${comment.id}')">수정</button>
                <button class="delete-button" onclick="deleteComment('${comment.id}')">삭제</button>
            </div>
            <div class="reply-form" style="display: none;">
                <input type="text" placeholder="답글을 입력하세요...">
                <button onclick="addReply('${comment.id}')">답글 등록</button>
            </div>
            <div class="replies"></div>
        `;
    commentsSection.appendChild(commentElement);
    fetchReplies(currentStudyId, comment.id); // 각 댓글의 대댓글을 가져오는 함수 호출
  });
}

// 댓글 수정 함수
function editComment(commentId) {
  const commentElement = document.querySelector(`#comment${commentId}`);
  const content = prompt('댓글을 수정하세요:',
      commentElement.querySelector('.comment-content').textContent);

  if (content !== null) {
    fetch(`/api/studies/${currentStudyId}/comments/${commentId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // Access Token 추가
      },
      body: JSON.stringify({content})
    })
    .then(response => response.json())
    .then(data => {
      if (data.statuscode === "200") {
        fetchComments(currentStudyId); // 댓글 목록 갱신
        Swal.fire({
          title: '댓글 수정 완료',
          text: '댓글이 성공적으로 수정되었습니다.',
          icon: 'success',
          confirmButtonText: '확인'
        });
      } else {
        console.error('댓글 수정 에러:', data.msg);
        Swal.fire({
          title: '댓글 수정 실패',
          text: '댓글 수정 중 오류가 발생했습니다.',
          icon: 'error',
          confirmButtonText: '확인'
        });
      }
    })
    .catch(error => {
      console.error('API 호출 중 에러 발생:', error);
      Swal.fire({
        title: '댓글 수정 실패',
        text: '네트워크 오류가 발생했습니다.',
        icon: 'error',
        confirmButtonText: '확인'
      });
    });
  }
}

// 대댓글을 가져오는 함수
function fetchReplies(studyId, commentId) {
  fetch(`/api/studies/${studyId}/comments/${commentId}/replies`)
  .then(response => response.json())
  .then(data => {
    if (data.statuscode === "200") {
      displayReplies(commentId, data.data);
    } else {
      console.error('대댓글 조회 에러:', data.msg);
    }
  })
  .catch(error => console.error('API 호출 중 에러 발생:', error));
}

// 대댓글을 화면에 표시하는 함수
function displayReplies(commentId, replies) {
  const repliesSection = document.querySelector(
      `#comment${commentId} .replies`);

  // 대댓글이 이미 존재하는지 확인하고 있으면 제거
  repliesSection.innerHTML = '';

  replies.forEach(reply => {
    const replyElement = document.createElement('div');
    replyElement.className = 'comment';
    replyElement.id = `reply${reply.id}`;
    replyElement.innerHTML = `
            <div class="comment-header">
                <div class="comment-author">${reply.author || '익명 사용자'}</div>
                <div class="comment-date">${new Date(
        reply.createdAt).toISOString().split('T')[0]}</div>
            </div>
            <div class="comment-content">${reply.content}</div>
            <div class="comment-actions">
                <button class="edit-button" onclick="editReply('${commentId}', '${reply.id}')">수정</button>
                <button class="delete-button" onclick="deleteReply('${commentId}', '${reply.id}')">삭제</button>
            </div>
        `;
    repliesSection.appendChild(replyElement);
  });
}

// 댓글 추가
function addComment() {
  const contentElement = document.getElementById('commentInput');
  const content = contentElement.value.trim(); // 입력된 내용의 공백을 제거

  if (!content) {
    Swal.fire({
      title: '댓글 추가 실패',
      text: '댓글 내용이 비어 있습니다.',
      icon: 'warning',
      confirmButtonText: '확인'
    });
    return;
  }

  fetch(`/api/studies/${currentStudyId}/comments`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // Access Token 추가
    },
    body: JSON.stringify({content})
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('서버 응답이 올바르지 않습니다.');
    }
    return response.json();
  })
  .then(data => {
    if (data.statuscode === "201") {
      contentElement.value = ''; // 입력 필드 비우기
      fetchComments(currentStudyId); // 댓글 목록 갱신
      Swal.fire({
        title: '댓글 추가 완료',
        text: '댓글이 성공적으로 추가되었습니다.',
        icon: 'success',
        confirmButtonText: '확인'
      });
    } else {
      throw new Error(data.msg || '댓글 추가 중 오류가 발생했습니다.');
    }
  })
  .catch(error => {
    console.error('API 호출 중 에러 발생:', error);
    Swal.fire({
      title: '댓글 추가 실패',
      text: error.message,
      icon: 'error',
      confirmButtonText: '확인'
    });
  });
}

// 댓글 삭제 함수
function deleteComment(commentId) {
  Swal.fire({
    title: '댓글 삭제',
    text: '정말로 이 댓글을 삭제하시겠습니까?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '삭제',
    cancelButtonText: '취소'
  }).then((result) => {
    if (result.isConfirmed) {
      fetch(`/api/studies/${currentStudyId}/comments/${commentId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        }
      })
      .then(response => response.json())
      .then(data => {
        if (data.statuscode === "204") {
          const commentElement = document.querySelector(`#comment${commentId}`);
          commentElement.remove();
          Swal.fire({
            title: '삭제 완료',
            text: '댓글이 성공적으로 삭제되었습니다.',
            icon: 'success',
            confirmButtonText: '확인'
          });
        } else {
          throw new Error(data.msg || '댓글 삭제 중 오류가 발생했습니다.');
        }
      })
      .catch(error => {
        console.error('API 호출 중 에러 발생:', error);
        Swal.fire({
          title: '삭제 실패',
          text: error.message,
          icon: 'error',
          confirmButtonText: '확인'
        });
      });
    }
  });
}

// 답글 폼을 표시하는 함수
function showReplyForm(commentId) {
  const form = document.querySelector(`#comment${commentId} .reply-form`);
  form.style.display = 'block';
}

// 대댓글 추가 함수
function addReply(commentId) {
  const replyInput = document.querySelector(
      `#comment${commentId} .reply-form input`);
  const replyContent = replyInput.value.trim(); // 입력 내용의 공백 제거

  // 입력 내용이 비어있는 경우
  if (!replyContent) {
    Swal.fire({
      title: '대댓글 추가 실패',
      text: '대댓글 내용이 비어있습니다.',
      icon: 'warning',
      confirmButtonText: '확인'
    });
    return;
  }

  fetch(`/api/studies/${currentStudyId}/comments/${commentId}/replies`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // Access Token 추가
    },
    body: JSON.stringify({content: replyContent})
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('서버 응답이 올바르지 않습니다.');
    }
    return response.json();
  })
  .then(data => {
    if (data.statuscode === "201") {
      replyInput.value = ''; // 입력 필드 비우기
      fetchReplies(currentStudyId, commentId); // 대댓글 목록 갱신
      Swal.fire({
        title: '대댓글 추가 완료',
        text: '대댓글이 성공적으로 추가되었습니다.',
        icon: 'success',
        confirmButtonText: '확인'
      });
    } else {
      throw new Error(data.msg || '대댓글 추가 중 오류가 발생했습니다.');
    }
  })
  .catch(error => {
    console.error('API 호출 중 에러 발생:', error);
    Swal.fire({
      title: '대댓글 추가 실패',
      text: error.message,
      icon: 'error',
      confirmButtonText: '확인'
    });
  });
}

// 대댓글 수정 함수
function editReply(commentId, replyId) {
  const replyElement = document.querySelector(`#reply${replyId}`);

  if (!replyElement) {
    console.error('대댓글 요소를 찾을 수 없습니다:', `#reply${replyId}`);
    return;
  }

  const content = prompt('대댓글을 수정하세요:',
      replyElement.querySelector('.comment-content').textContent);

  if (content !== null) {
    fetch(
        `/api/studies/${currentStudyId}/comments/${commentId}/replies/${replyId}`,
        {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // Access Token 추가
          },
          body: JSON.stringify({content})
        })
    .then(response => response.json())
    .then(data => {
      if (data.statuscode === "200") {
        fetchReplies(currentStudyId, commentId); // 대댓글 목록 갱신
        Swal.fire({
          title: '대댓글 수정 완료',
          text: '대댓글이 성공적으로 수정되었습니다.',
          icon: 'success',
          confirmButtonText: '확인'
        });
      } else {
        console.error('대댓글 수정 에러:', data.msg);
        Swal.fire({
          title: '대댓글 수정 실패',
          text: '대댓글 수정 중 오류가 발생했습니다.',
          icon: 'error',
          confirmButtonText: '확인'
        });
      }
    })
    .catch(error => {
      console.error('API 호출 중 에러 발생:', error);
      Swal.fire({
        title: '대댓글 수정 실패',
        text: '네트워크 오류가 발생했습니다.',
        icon: 'error',
        confirmButtonText: '확인'
      });
    });
  }
}

// 대댓글 삭제 함수
function deleteReply(commentId, replyId) {
  if (!replyId) {
    console.error('대댓글 ID가 제공되지 않았습니다.');
    Swal.fire({
      title: '대댓글 삭제 실패',
      text: '대댓글 ID가 누락되었습니다.',
      icon: 'error',
      confirmButtonText: '확인'
    });
    return;
  }

  if (confirm('정말로 이 대댓글을 삭제하시겠습니까?')) {
    fetch(`/api/studies/${currentStudyId}/comments/${commentId}/replies/${replyId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // Access Token 추가
      }
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('서버 응답이 올바르지 않습니다.');
      }
      return response.json();
    })
    .then(data => {
      if (data.statuscode === "204") {
        fetchReplies(currentStudyId, commentId); // 대댓글 목록 갱신
        Swal.fire({
          title: '대댓글 삭제 완료',
          text: '대댓글이 성공적으로 삭제되었습니다.',
          icon: 'success',
          confirmButtonText: '확인'
        });
      } else {
        throw new Error(data.msg || '대댓글 삭제 중 오류가 발생했습니다.');
      }
    })
    .catch(error => {
      console.error('API 호출 중 에러 발생:', error);
      Swal.fire({
        title: '대댓글 삭제 실패',
        text: error.message,
        icon: 'error',
        confirmButtonText: '확인'
      });
    });
  }
}