// 필터 토글 기능
const filterToggle = document.querySelector('.filter-toggle');
const filterPanel = document.querySelector('.filter-panel');

filterToggle.addEventListener('click', function () {
  if (filterPanel.style.display === 'none' || filterPanel.style.display
      === '') {
    filterPanel.style.display = 'block';
    filterToggle.textContent = '상세 필터 ▲';
  } else {
    filterPanel.style.display = 'none';
    filterToggle.textContent = '상세 필터 ▼';
  }
});

// 페이지네이션 기능
document.querySelectorAll('.pagination button').forEach(button => {
  button.addEventListener('click', function () {
    if (!this.classList.contains('active')) {
      document.querySelector('.pagination button.active').classList.remove(
          'active');
      this.classList.add('active');
    }
  });
});

// 좋아요 버튼 기능
document.querySelectorAll('.like-button').forEach(button => {
  button.addEventListener('click', function (e) {
    e.stopPropagation();
    this.classList.toggle('liked');
  });
});

// 리뷰 아이템 호버 애니메이션
document.querySelectorAll('.ranking-item').forEach(item => {
  item.addEventListener('mouseenter', function () {
    this.style.transform = 'translateY(-5px)';
    this.style.boxShadow = '0 5px 15px rgba(0,0,0,0.1)';
  });
  item.addEventListener('mouseleave', function () {
    this.style.transform = 'translateY(0)';
    this.style.boxShadow = 'none';
  });
  item.addEventListener('click', function () {
    console.log('리뷰 상세 페이지로 이동:', this.querySelector('h3').textContent);
  });
});

// 정렬 기능
document.querySelector('.ranking-sort select').addEventListener('change',
    function () {
      console.log('정렬 방식 변경:', this.value);
    });

// StudyTrek 로고 클릭 기능
document.querySelector('.logo').addEventListener('click', function () {
  window.location.href = '/';
});


// DOMContentLoaded 이벤트가 발생하면 필터링된 결과를 업데이트합니다.
document.addEventListener('DOMContentLoaded', () => { // filteredResults
  const resultsContainer = document.getElementById('resultsContainer');
  const filteredResults = JSON.parse(localStorage.getItem('filteredResults'));

  if (filteredResults) {
    updateResults(filteredResults); // 필터링된 결과를 화면에 업데이트합니다.
    localStorage.removeItem('filteredResults'); // 필터링된 결과를 로컬 스토리지에서 제거합니다.
  }

  // 결과를 업데이트하여 화면에 표시합니다.
  function updateResults(data) {
    resultsContainer.innerHTML = ''; // 기존 결과를 지웁니다.

    data.forEach(rank => {
      const camp = rank.camp; // RankResponseDto의 camp 필드
      const track = camp.track;
      const environment = camp.environment;
      const cost = camp.cost;
      const name = camp.name;

      const resultItem = document.createElement('div');
      resultItem.classList.add('ranking-item');
      resultItem.setAttribute('data-track', track);
      resultItem.setAttribute('data-environment', environment);
      resultItem.setAttribute('data-cost', cost);

      // 결과 항목의 HTML을 생성하여 추가합니다.
      resultItem.innerHTML = `
                <button class="like-button">❤</button>
                <h3>${name}</h3>
                <div class="rating">★★★★☆</div>
                <div class="review-preview">
                    "실무에 바로 적용할 수 있는 skills을 배웠습니다. 다만, 속도가 조금 빨랐어요."
                </div>
                <div class="review-meta">
                    작성자: John D. | 날짜: 2023-05-15
                </div>
            `;

      resultsContainer.appendChild(resultItem); // 결과 항목을 결과 컨테이너에 추가합니다.
    });
    applyFilters(); // 필터를 적용하여 화면에 표시할 항목을 결정합니다.
  }
});

// 필터를 적용하여 화면에 표시할 항목을 결정합니다.
function applyFilters() {
  const filterRequest = getFilterRequest(); // 현재 필터 요청을 가져옵니다.
  console.log('Applying filters:', filterRequest);

  const items = document.querySelectorAll('.ranking-item');
  items.forEach(item => {
    const track = item.getAttribute('data-track');
    const environment = item.getAttribute('data-environment');
    const cost = item.getAttribute('data-cost');

    let visible = true;

    // 필터 요청에 따라 항목의 표시 여부를 결정합니다.
    if (filterRequest.track && filterRequest.track !== track) {
      visible = false;
    }
    if (filterRequest.environment && filterRequest.environment !== environment) {
      visible = false;
    }
    if (filterRequest.cost && filterRequest.cost !== cost) {
      visible = false;
    }

    item.style.display = visible ? 'block' : 'none'; // 항목의 표시 여부를 설정합니다.
  });
}