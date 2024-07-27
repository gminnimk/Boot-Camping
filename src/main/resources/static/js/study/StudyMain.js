document.addEventListener('DOMContentLoaded', function () {
    const sortButtons = document.querySelectorAll('.filter-btn[data-sort]');
    const trackButtons = document.querySelectorAll('.filter-btn[data-track]');
    const heartIcons = document.querySelectorAll('.heart-icon');
    const startReviewBtn = document.querySelector('.start-review-btn'); // 스터디 모집글 작성 버튼

    sortButtons.forEach(button => {
        button.addEventListener('click', function () {
            const sortType = this.dataset.sort;

            sortButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');

            console.log(`Sorting by: ${sortType}`);
            sortContent(sortType);
        });
    });

    trackButtons.forEach(button => {
        button.addEventListener('click', function () {
            const trackType = this.dataset.track;

            trackButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');

            console.log(`Filtering by track: ${trackType}`);
            filterByTrack(trackType);
        });
    });

    heartIcons.forEach(icon => {
        icon.addEventListener('click', function () {
            this.textContent = this.textContent === '♡' ? '♥' : '♡';
        });
    });

    // 버튼 클릭 이벤트 리스너
    startReviewBtn.addEventListener('click', function() {
        const url = this.dataset.url;
        window.location.href = url;
    });

    function sortContent(sortType) {
        console.log(`Sorting content by: ${sortType}`);
        // Implement your sorting logic here
    }

    function filterByTrack(trackType) {
        console.log(`Filtering content by track: ${trackType}`);
        // Implement your filtering logic here
    }
});
