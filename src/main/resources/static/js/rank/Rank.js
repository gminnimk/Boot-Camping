// 필터 토글 기능
const filterToggle = document.querySelector('.filter-toggle');
const filterPanel = document.querySelector('.filter-panel');

filterToggle.addEventListener('click', function() {
    if (filterPanel.style.display === 'none' || filterPanel.style.display === '') {
        filterPanel.style.display = 'block';
        filterToggle.textContent = '상세 필터 ▲';
    } else {
        filterPanel.style.display = 'none';
        filterToggle.textContent = '상세 필터 ▼';
    }
});

// 페이지네이션 기능
document.querySelectorAll('.pagination button').forEach(button => {
    button.addEventListener('click', function() {
        if (!this.classList.contains('active')) {
            document.querySelector('.pagination button.active').classList.remove('active');
            this.classList.add('active');
        }
    });
});

// 좋아요 버튼 기능
document.querySelectorAll('.like-button').forEach(button => {
    button.addEventListener('click', function(e) {
        e.stopPropagation();
        this.classList.toggle('liked');
    });
});

// 리뷰 아이템 호버 애니메이션
document.querySelectorAll('.ranking-item').forEach(item => {
    item.addEventListener('mouseenter', function() {
        this.style.transform = 'translateY(-5px)';
        this.style.boxShadow = '0 5px 15px rgba(0,0,0,0.1)';
    });
    item.addEventListener('mouseleave', function() {
        this.style.transform = 'translateY(0)';
        this.style.boxShadow = 'none';
    });
    item.addEventListener('click', function() {
        console.log('리뷰 상세 페이지로 이동:', this.querySelector('h3').textContent);
    });
});

// 정렬 기능
document.querySelector('.ranking-sort select').addEventListener('change', function() {
    console.log('정렬 방식 변경:', this.value);
});

// 로그인 버튼 기능
document.querySelector('.login-button').addEventListener('click', function() {
    console.log('로그인 페이지로 이동');
    // 여기에 로그인 페이지로 이동하는 코드를 추가할 수 있습니다.
});

// StudyTrek 로고 클릭 기능
document.querySelector('.logo').addEventListener('click', function() {
    window.location.href = '/';
});
