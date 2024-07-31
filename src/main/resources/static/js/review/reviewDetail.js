let currentReviewId;

document.addEventListener('DOMContentLoaded', function() {
    const pathSegments = window.location.pathname.split('/');
    currentReviewId = pathSegments[pathSegments.length - 1]; // URL에서 리뷰 ID 추출

    if (currentReviewId) {
        fetchReviewDetails(currentReviewId);
    } else {
        console.error('리뷰 ID가 제공되지 않았습니다.');
    }

    addHeartButtonListeners();
});

// 리뷰 상세 정보를 가져오는 함수
function fetchReviewDetails(id) {
    fetch(`/api/reviews/${id}`)
    .then(response => response.json())
    .then(data => {
        if (data.statuscode === "200") {
            updateReviewDetails(data.data);
        } else {
            console.error('에러:', data.msg);
        }
    })
    .catch(error => console.error('API 호출 중 에러 발생:', error));
}

// 리뷰 상세 정보를 화면에 표시하는 함수
function updateReviewDetails(review) {
    document.querySelector('.review-title').textContent = review.title;
    document.querySelector('#subtitleDisplay').textContent = review.campName;
    const categories = document.querySelector('.meta-info div');
    categories.innerHTML = `<span class="category">${review.category}</span><span class="category">${review.trek}</span>`;
    document.querySelector('.date').textContent = new Date(review.createdAt).toLocaleDateString();
    document.querySelector('.rating').textContent = '★'.repeat(review.scope) + '☆'.repeat(5 - review.scope);
    document.querySelector('.detail-content').innerHTML = review.content.replace(/\n/g, '<br>');
    document.querySelector('.author').textContent = `- ${review.author || '익명'}`;

    // 수정 폼의 초기값 설정
    document.getElementById('editTitle').value = review.title;
    document.getElementById('editSubtitle').value = review.campName;
    document.getElementById('editCategory').value = review.category;
    document.getElementById('editDevCategory').value = review.trek;
    document.getElementById('editRating').value = review.scope;
    document.getElementById('editContent').value = review.content;
}

// 리뷰 수정 함수
function saveEdit() {
    if (confirm('정말 수정하시겠습니까?')) {
        const updatedReview = {
            title: document.getElementById('editTitle').value,
            campName: document.getElementById('editSubtitle').value,
            category: document.getElementById('editCategory').value,
            trek: document.getElementById('editDevCategory').value,
            scope: parseInt(document.getElementById('editRating').value, 10), // 별점 숫자로 변환
            content: document.getElementById('editContent').value
        };

        fetch(`/api/reviews/${currentReviewId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // Access Token 추가
            },
            body: JSON.stringify(updatedReview)
        })
        .then(response => response.json())
        .then(data => {
            if (data.statuscode === "200") {
                updateReviewDetails(data.data);
                document.getElementById('viewMode').style.display = 'block';
                document.getElementById('editMode').style.display = 'none';
                Swal.fire({
                    title: '수정 완료',
                    text: '리뷰가 성공적으로 수정되었습니다.',
                    icon: 'success',
                    confirmButtonText: '확인'
                });
            } else {
                console.error('에러:', data.msg);
                Swal.fire({
                    title: '수정 실패',
                    text: '리뷰 수정 중 오류가 발생했습니다.',
                    icon: 'error',
                    confirmButtonText: '확인'
                });
            }
        })
        .catch(error => {
            console.error('API 호출 중 에러 발생:', error);
            Swal.fire({
                title: '수정 실패',
                text: '네트워크 오류가 발생했습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        });
    }
}

// 기타 함수 (좋아요 버튼 리스너, 댓글 추가 등) 는 기존과 동일
document.addEventListener('DOMContentLoaded', function() {
    const pathSegments = window.location.pathname.split('/');
    currentReviewId = pathSegments[pathSegments.length - 1]; // URL에서 리뷰 ID 추출

    if (currentReviewId) {
        fetchReviewDetails(currentReviewId);
    } else {
        console.error('리뷰 ID가 제공되지 않았습니다.');
    }

    addHeartButtonListeners();
});

// 리뷰 상세 정보를 가져오는 함수
function fetchReviewDetails(id) {
    fetch(`/api/reviews/${id}`)
    .then(response => response.json())
    .then(data => {
        if (data.statuscode === "200") {
            updateReviewDetails(data.data);
        } else {
            console.error('에러:', data.msg);
        }
    })
    .catch(error => console.error('API 호출 중 에러 발생:', error));
}

// 리뷰 상세 정보를 화면에 표시하는 함수
function updateReviewDetails(review) {
    document.querySelector('.review-title').textContent = review.title;
    document.querySelector('#subtitleDisplay').textContent = review.campName;
    const categories = document.querySelector('.meta-info div');
    categories.innerHTML = `<span class="category">${review.category}</span><span class="category">${review.trek}</span>`;
    document.querySelector('.date').textContent = new Date(review.createdAt).toLocaleDateString();
    document.querySelector('.rating').textContent = '★'.repeat(review.scope) + '☆'.repeat(5 - review.scope);
    document.querySelector('.detail-content').innerHTML = review.content.replace(/\n/g, '<br>');
    document.querySelector('.author').textContent = `- ${review.author || '익명'}`;

    // 수정 폼의 초기값 설정
    document.getElementById('editTitle').value = review.title;
    document.getElementById('editSubtitle').value = review.campName;
    document.getElementById('editCategory').value = review.category;
    document.getElementById('editDevCategory').value = review.trek;
    document.getElementById('editRating').value = review.scope;
    document.getElementById('editContent').value = review.content;
}

// 리뷰 수정 함수
function saveEdit() {
    if (confirm('정말 수정하시겠습니까?')) {
        const updatedReview = {
            title: document.getElementById('editTitle').value,
            campName: document.getElementById('editSubtitle').value,
            category: document.getElementById('editCategory').value,
            trek: document.getElementById('editDevCategory').value,
            scope: parseInt(document.getElementById('editRating').value, 10), // 별점 숫자로 변환
            content: document.getElementById('editContent').value
        };

        fetch(`/api/reviews/${currentReviewId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // Access Token 추가
            },
            body: JSON.stringify(updatedReview)
        })
        .then(response => response.json())
        .then(data => {
            if (data.statuscode === "200") {
                updateReviewDetails(data.data);
                document.getElementById('viewMode').style.display = 'block';
                document.getElementById('editMode').style.display = 'none';
                Swal.fire({
                    title: '수정 완료',
                    text: '리뷰가 성공적으로 수정되었습니다.',
                    icon: 'success',
                    confirmButtonText: '확인'
                });
            } else {
                console.error('에러:', data.msg);
                Swal.fire({
                    title: '수정 실패',
                    text: '리뷰 수정 중 오류가 발생했습니다.',
                    icon: 'error',
                    confirmButtonText: '확인'
                });
            }
        })
        .catch(error => {
            console.error('API 호출 중 에러 발생:', error);
            Swal.fire({
                title: '수정 실패',
                text: '네트워크 오류가 발생했습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        });
    }
}

// 리뷰 삭제 함수
function reviewDel() {
    if (confirm('정말로 이 리뷰를 삭제하시겠습니까?')) {
        fetch(`/api/reviews/${currentReviewId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // Access Token 추가
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.statuscode === "204") { // 성공적으로 삭제됨
                Swal.fire({
                    title: '삭제 완료',
                    text: '리뷰가 성공적으로 삭제되었습니다.',
                    icon: 'success',
                    confirmButtonText: '확인'
                }).then(() => {
                    window.location.href = '/review'; // 리뷰 목록 페이지로 리디렉션
                });
            } else {
                console.error('에러:', data.msg);
                Swal.fire({
                    title: '삭제 실패',
                    text: '리뷰 삭제 중 오류가 발생했습니다.',
                    icon: 'error',
                    confirmButtonText: '확인'
                });
            }
        })
        .catch(error => {
            console.error('API 호출 중 에러 발생:', error);
            Swal.fire({
                title: '삭제 실패',
                text: '네트워크 오류가 발생했습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        });
    }
}

// 좋아요 버튼 리스너 추가 함수
function addHeartButtonListeners() {
    document.querySelectorAll('.heart-button').forEach(button => {
        button.addEventListener('click', function(event) {
            event.stopPropagation();
            const icon = this.querySelector('i');
            if (icon.classList.contains('far')) {
                icon.classList.remove('far');
                icon.classList.add('fas');
            } else {
                icon.classList.remove('fas');
                icon.classList.add('far');
            }
        });
    });
}

// 댓글 추가 함수
function addComment() {
    const commentText = document.getElementById('commentInput').value;
    if (commentText.trim() !== '') {
        const commentsSection = document.querySelector('.comments-section');
        const newComment = document.createElement('div');
        newComment.className = 'comment';
        const commentId = 'comment' + (document.querySelectorAll('.comment').length + 1);
        newComment.id = commentId;
        newComment.innerHTML = `
            <div class="comment-header">
                <div class="comment-author">익명 사용자</div>
                <div class="comment-date">${new Date().toISOString().split('T')[0]}</div>
            </div>
            <div class="comment-content">${commentText}</div>
            <div class="comment-actions">
                <button class="reply-button" onclick="showReplyForm('${commentId}')">답글</button>
                <button class="edit-button" onclick="editComment('${commentId}')">수정</button>
                <button class="delete-button" onclick="deleteComment('${commentId}')">삭제</button>
            </div>
            <div class="reply-form" style="display: none;">
                <input type="text" placeholder="답글을 입력하세요...">
                <button onclick="addReply('${commentId}')">답글 등록</button>
            </div>
            <div class="replies"></div>
        `;
        commentsSection.insertBefore(newComment, document.querySelector('.comment'));
        document.getElementById('commentInput').value = '';
    } else {
        alert('댓글 내용을 입력해주세요.');
    }
}

// 답글 폼 표시 함수
function showReplyForm(commentId) {
    const replyForm = document.querySelector(`#${commentId} .reply-form`);
    replyForm.style.display = replyForm.style.display === 'none' ? 'block' : 'none';
}

// 답글 추가 함수
function addReply(commentId) {
    const replyForm = document.querySelector(`#${commentId} .reply-form`);
    const replyText = replyForm.querySelector('input').value;
    if (replyText.trim() !== '') {
        const repliesSection = document.querySelector(`#${commentId} .replies`);
        const newReply = document.createElement('div');
        newReply.className = 'comment';
        const replyId = `${commentId}-reply${repliesSection.children.length + 1}`;
        newReply.id = replyId;
        newReply.innerHTML = `
            <div class="comment-header">
                <div class="comment-author">익명 사용자</div>
                <div class="comment-date">${new Date().toISOString().split('T')[0]}</div>
            </div>
            <div class="comment-content">${replyText}</div>
            <div class="comment-actions">
                <button class="edit-button" onclick="editComment('${replyId}')">수정</button>
                <button class="delete-button" onclick="deleteComment('${replyId}')">삭제</button>
            </div>
        `;
        repliesSection.appendChild(newReply);
        replyForm.querySelector('input').value = '';
        replyForm.style.display = 'none';
    } else {
        alert('답글 내용을 입력해주세요.');
    }
}

// 댓글 수정 함수
function editComment(commentId) {
    const commentContent = document.querySelector(`#${commentId} .comment-content`);
    const currentContent = commentContent.textContent;
    const newContent = prompt('댓글을 수정하세요:', currentContent);
    if (newContent !== null && newContent.trim() !== '') {
        commentContent.textContent = newContent;
    }
}

// 댓글 삭제 함수
function deleteComment(commentId) {
    if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
        const comment = document.getElementById(commentId);
        comment.remove();
    }
}

// 수정 폼 보기 함수
function showEditForm() {
    document.getElementById('viewMode').style.display = 'none';
    document.getElementById('editMode').style.display = 'block';
}

// 수정 취소 함수
function cancelEdit() {
    if (confirm('수정을 취소하시겠습니까? 변경사항이 저장되지 않습니다.')) {
        document.getElementById('viewMode').style.display = 'block';
        document.getElementById('editMode').style.display = 'none';
    }
}