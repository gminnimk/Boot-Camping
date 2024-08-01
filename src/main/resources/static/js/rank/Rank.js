// 페이지당 항목 수와 현재 페이지 변수
const ITEMS_PER_PAGE = 9;
let currentPage = 1;
let totalPages = 1;
let allResults = [];

// 페이지네이션 기능을 업데이트합니다.
function updatePagination() {
  const paginationContainer = document.getElementById('pageNumbers');
  const prevButton = document.getElementById('prevButton');
  const nextButton = document.getElementById('nextButton');

  paginationContainer.innerHTML = ''; // 기존 페이지 번호를 지웁니다.

  // 페이지 번호 추가
  for (let i = 1; i <= totalPages; i++) {
    const pageNumber = document.createElement('button');
    pageNumber.textContent = i;
    pageNumber.classList.add('page-number');
    if (i === currentPage) {
      pageNumber.classList.add('active');
    }
    pageNumber.addEventListener('click', () => {
      currentPage = i;
      updateResultsForCurrentPage();
    });
    paginationContainer.appendChild(pageNumber);
  }

  // Previous 버튼 상태 업데이트
  prevButton.disabled = currentPage === 1;

  // Next 버튼 상태 업데이트
  nextButton.disabled = currentPage === totalPages;
}

// 현재 페이지에 맞는 결과만 업데이트합니다.
function updateResultsForCurrentPage() {
  const resultsContainer = document.getElementById('resultsContainer');
  resultsContainer.innerHTML = ''; // 기존 결과를 지웁니다.

  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const endIndex = Math.min(startIndex + ITEMS_PER_PAGE, allResults.length);

  for (let i = startIndex; i < endIndex; i++) {
    const rank = allResults[i];
    const camp = rank.camp;
    const track = camp.track;
    const environment = camp.environment;
    const cost = camp.cost;
    const name = camp.name;

    const resultItem = document.createElement('div');
    resultItem.classList.add('ranking-item');
    resultItem.setAttribute('data-track', track);
    resultItem.setAttribute('data-environment', environment);
    resultItem.setAttribute('data-cost', cost);

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

    resultsContainer.appendChild(resultItem);
  }

  applyLikeButtonListeners(); // 새로 추가된 항목에 좋아요 버튼 리스너 적용
  applyHoverAnimationListeners(); // 새로 추가된 항목에 호버 애니메이션 리스너 적용
  updatePagination(); // 페이지네이션 업데이트
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

// 페이지 이동 함수
function changePage(direction) {
  if (direction === -1 && currentPage > 1) {
    currentPage--;
  } else if (direction === 1 && currentPage < totalPages) {
    currentPage++;
  }
  updateResultsForCurrentPage();
}

// 결과를 업데이트하여 화면에 표시합니다.
function updateResults(data) {
  allResults = data; // 전체 결과를 저장합니다.
  totalPages = Math.ceil(allResults.length / ITEMS_PER_PAGE); // 총 페이지 수 계산
  currentPage = 1; // 페이지 초기화
  updateResultsForCurrentPage(); // 현재 페이지의 결과만 업데이트
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
  const track = document.querySelector('input[name="category"]:checked')?.value
      || '';
  const environment = '';
  const cost = '';

  return {track, environment, cost};
}