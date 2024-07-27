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
            <div class="reply-form">
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

function showReplyForm(commentId) {
    const replyForm = document.querySelector(`#${commentId} .reply-form`);
    replyForm.style.display = replyForm.style.display === 'none' ? 'block' : 'none';
}

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

function editComment(commentId) {
    const commentContent = document.querySelector(`#${commentId} .comment-content`);
    const currentContent = commentContent.textContent;
    const newContent = prompt('댓글을 수정하세요:', currentContent);
    if (newContent !== null && newContent.trim() !== '') {
        commentContent.textContent = newContent;
    }
}

function deleteComment(commentId) {
    if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
        const comment = document.getElementById(commentId);
        comment.remove();
    }
}

function showEditForm() {
    document.getElementById('viewMode').style.display = 'none';
    document.getElementById('editMode').style.display = 'block';
}

function cancelEdit() {
    document.getElementById('viewMode').style.display = 'block';
    document.getElementById('editMode').style.display = 'none';
}

function saveEdit() {
    const title = document.getElementById('editTitle').value;
    const subtitle = document.getElementById('editSubtitle').value;
    const category = document.getElementById('editCategory').value;
    const devCategory = document.getElementById('editDevCategory').value;
    const rating = document.getElementById('editRating').value;
    const content = document.getElementById('editContent').value;

    // Here you would typically send this data to the server
    // For this example, we'll just update the view
    document.querySelector('.review-title').textContent = title;
    document.querySelector('.review-subtitle').textContent = subtitle;
    const categories = document.querySelector('.meta-info').querySelector('div');
    categories.innerHTML = `<span class="category">${category}</span><span class="category">${devCategory}</span>`;
    document.querySelector('.rating').textContent = '★'.repeat(rating) + '☆'.repeat(5 - rating);
    document.querySelector('.content').innerHTML = content.replace(/\n/g, '<br>');

    cancelEdit();
    return false; // Prevent form submission
}
document.addEventListener('DOMContentLoaded', addHeartButtonListeners);