function goToMainPage() {
    console.log("메인 페이지로 이동");
    // 실제로는 window.location.href = "메인페이지URL"; 를 사용할 것입니다.
}

function editStudy() {
    console.log("스터디 정보 수정");
    // 여기에 수정 로직을 구현합니다.
}

function deleteStudy() {
    if (confirm("정말로 이 스터디를 삭제하시겠습니까?")) {
        console.log("스터디 삭제");
        // 여기에 삭제 로직을 구현합니다.
    }
}

function editComment(commentId) {
    console.log("댓글 " + commentId + " 수정");
    // 여기에 댓글 수정 로직을 구현합니다.
}

function deleteComment(commentId) {
    if (confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
        console.log("댓글 " + commentId + " 삭제");
        // 여기에 댓글 삭제 로직을 구현합니다.
    }
}

function toggleReplyForm(commentId) {
    const replyForm = document.getElementById('replyForm' + commentId);
    replyForm.style.display = replyForm.style.display === 'none' ? 'block' : 'none';
}

function submitReply(commentId) {
    const replyText = document.querySelector('#replyForm' + commentId + ' textarea').value;
    console.log("댓글 " + commentId + "에 답글 작성: " + replyText);
    // 여기에 답글 제출 로직을 구현합니다.
}

document.querySelector('.comment-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const name = this.querySelector('input[type="text"]').value;
    const comment = this.querySelector('textarea').value;
    console.log("새 댓글 작성: " + name + " - " + comment);
    // 여기에 새 댓글 제출 로직을 구현합니다.
});

