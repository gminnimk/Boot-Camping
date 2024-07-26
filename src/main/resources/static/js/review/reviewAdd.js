document.getElementById('reviewForm').addEventListener('submit', function(e) {
    e.preventDefault();
    // 선택된 카테고리와 트랙을 수집합니다.
    const selectedCategory = document.querySelector('input[name="category"]:checked').value;
    const selectedTrack = document.querySelector('input[name="track"]:checked').value;
    const reviewTitle = document.getElementById('review-title').value;
    const rating = document.querySelector('input[name="rating"]:checked').value;
    const reviewContent = document.getElementById('review-content').value;

    // 여기에서 수집된 데이터를 서버로 전송하는 로직을 구현할 수 있습니다.
    console.log('Selected Category:', selectedCategory);
    console.log('Selected Track:', selectedTrack);
    console.log('Review Title:', reviewTitle);
    console.log('Rating:', rating);
    console.log('Review Content:', reviewContent);

    // 예시로 alert를 사용하여 제출 완료를 알립니다.
    alert('리뷰가 성공적으로 제출되었습니다!');
    // 제출 후 리뷰 목록 페이지로 리디렉션합니다.
    window.location.href = 'https://reviewsite.com/reviews';
});

function cancelReview() {
    if (confirm('작성 중인 리뷰를 취소하시겠습니까?')) {
        const cancelUrl = document.querySelector('.cancel-button').getAttribute('data-cancel-url');
        window.location.href = cancelUrl;
    }
}