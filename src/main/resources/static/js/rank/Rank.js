// 필터 토글 기능
document.addEventListener('DOMContentLoaded', function() {
  const filterToggle = document.querySelector('.filter-toggle');
  const filterPanel = document.querySelector('.filter-panel');

  if (filterToggle && filterPanel) {  // 요소가 존재하는지 확인
    filterToggle.addEventListener('click', function () {
      if (filterPanel.style.display === 'none' || filterPanel.style.display === '') {
        filterPanel.style.display = 'block';
        filterToggle.textContent = '상세 필터 ▲';
      } else {
        filterPanel.style.display = 'none';
        filterToggle.textContent = '상세 필터 ▼';
      }
    });
  }
});

// API를 호출하여 필터링된 결과를 가져옵니다.
function fetchFilteredResults(filterRequest) {
  const queryString = new URLSearchParams(filterRequest).toString();
  fetch(`/ranks/filter?${queryString}`)
      .then(response => response.json())
      .then(data => {
        updateResults(data);
      })
      .catch(error => console.error('Error fetching ranks:', error));
}

// 필터를 적용하여 화면에 표시할 항목을 결정합니다.
function applyFilters() {
  const filterRequest = getFilterRequest(); // 현재 필터 요청을 가져옵니다.
  fetchFilteredResults(filterRequest); // 필터링된 결과를 가져옵니다.
  console.log('Applying filters:', filterRequest);
}

// 페이지네이션 기능
function applyPaginationListeners() {
  const paginationButtons = document.querySelectorAll('.pagination button');
  if (paginationButtons.length > 0) {
    paginationButtons.forEach(button => {
      button.addEventListener('click', function () {
        if (!this.classList.contains('active')) {
          document.querySelector('.pagination button.active')?.classList.remove('active');
          this.classList.add('active');
        }
      });
    });
  }
}

// 좋아요 버튼 기능
function applyLikeButtonListeners() {
  const likeButtons = document.querySelectorAll('.like-button');
  if (likeButtons.length > 0) {
    likeButtons.forEach(button => {
      button.addEventListener('click', function (e) {
        e.stopPropagation();
        this.classList.toggle('liked');
      });
    });
  }
}

// 리뷰 아이템 호버 애니메이션
function applyHoverAnimationListeners() {
  const rankingItems = document.querySelectorAll('.ranking-item');
  if (rankingItems.length > 0) {
    rankingItems.forEach(item => {
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
  }
}

// 결과를 업데이트하여 화면에 표시합니다.
function updateResults(data) {
  const resultsContainer = document.getElementById('resultsContainer');
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
      <div class="rating">Ranking: ${rank.ranking}</div>
      <div class="review-preview">
        "${camp.description}"
      </div>
      <div class="review-meta">
        작성자: John D. | 날짜: 2023-05-15
      </div>
    `;

    resultsContainer.appendChild(resultItem); // 결과 항목을 결과 컨테이너에 추가합니다.
  });

  applyLikeButtonListeners(); // 새로 추가된 항목에 좋아요 버튼 리스너 적용
  applyHoverAnimationListeners(); // 새로 추가된 항목에 호버 애니메이션 리스너 적용
  applyPaginationListeners(); // 새로 추가된 항목에 페이지네이션 리스너 적용
}

// DOMContentLoaded 이벤트가 발생하면 필터링된 결과를 업데이트합니다.
document.addEventListener('DOMContentLoaded', () => {
  const filteredResults = JSON.parse(localStorage.getItem('filteredResults'));

  if (filteredResults) {
    updateResults(filteredResults); // 필터링된 결과를 화면에 업데이트합니다.
    localStorage.removeItem('filteredResults'); // 필터링된 결과를 로컬 스토리지에서 제거합니다.
  } else {
    applyFilters(); // 필터링된 결과가 없을 경우 초기 필터 적용
  }
});

// 정렬 기능
const rankingSortSelect = document.querySelector('.ranking-sort select');
if (rankingSortSelect) {
  rankingSortSelect.addEventListener('change', function () {
    console.log('정렬 방식 변경:', this.value);
    applyFilters(); // 정렬 변경 시 필터를 재적용합니다.
  });
}

// 현재 필터 요청을 생성하는 함수
function getFilterRequest() {
  const track = document.querySelector('input[name="category"]:checked')?.value || '';
  const environment = '';
  const cost = '';

  return { track, environment, cost };
}
