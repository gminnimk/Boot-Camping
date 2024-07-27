function goToMainPage() {
    // 메인 페이지로 이동하는 함수 구현
    window.location.href = 'https://studytrek.com/';
}

document.getElementById('studyForm').addEventListener('submit', function(e) {
    e.preventDefault();
    // 여기서 폼 데이터를 서버로 전송하는 로직을 구현합니다.
    console.log("Form submitted");
    alert("스터디 모집글이 등록되었습니다!");
    // 등록 후 메인 페이지로 리다이렉트
    window.location.href = 'https://studytrek.com/';
});
