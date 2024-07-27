function goToMainPage() {
    window.location.href = 'https://studytrek.com/';
}

document.getElementById('studyForm').addEventListener('submit', function(e) {
    e.preventDefault();
    // 여기에 수정된 내용을 서버로 전송하는 로직을 구현합니다.
    console.log('스터디 모집글이 수정되었습니다.');
    // 수정 완료 후 상세 페이지로 리다이렉트
    window.location.href = 'https://studytrek.com/study/detail';
});

function cancelEdit() {
    if (confirm('수정을 취소하시겠습니까? 변경사항이 저장되지 않습니다.')) {
        // 상세 페이지로 돌아갑니다.
        window.location.href = 'https://studytrek.com/study/detail';
    }
}
