const ITEMS_PER_PAGE = 9;  // 한 페이지당 항목 수
let currentPage = 1;
let totalPages = 1;
let totalStudies = 0;

// API에서 스터디 데이터를 가져오는 함수
function fetchStudies() {
  return fetch(`/api/studies?page=${currentPage - 1}&size=${ITEMS_PER_PAGE}`)
  .then(response => response.json())
  .then(data => {
    if (data.statuscode === "200") {
      totalStudies = data.data.totalElements;
      totalPages = data.data.totalPages;
      return data.data.content;
    } else {
      console.error('에러:', data.msg);
      return [];
    }
  })
  .catch(error => {
    console.error('API 호출 중 에러 발생:', error);
    return [];
  });
}

// 스터디 카드 생성 함수
function createStudyCard(study) {
  return `
    <div class="card" data-study-id="${study.id}">
      <div class="card-header">
        <h3 class="card-title">${study.title}</h3>
        <span class="tag">${study.category}</span>
      </div>
      <span class="date">${formatDate(study.createdAt)}</span>
      <div class="card-content">
        <p>${study.content}</p>
      </div>
      <div class="author">${study.author || '익명 사용자'}</div>
      <div class="card-footer">
        <button class="heart-button">
          <i class="far fa-heart"></i>
        </button>
      </div>
    </div>
  `;
}

// 페이지 렌더링 함수
function renderPage() {
  fetchStudies().then(studies => {
    const container = document.getElementById('study-card-container');
    container.innerHTML = '';
    studies.forEach(study => {
      container.innerHTML += createStudyCard(study);
    });
    updatePaginationButtons();
    renderPageNumbers();
    addHeartButtonListeners();
    addCardClickListeners();
  });
}

// 페이지 변경 함수
function changePage(direction) {
  if ((direction === -1 && currentPage > 1) || (direction === 1 && currentPage
      < totalPages)) {
    currentPage += direction;
    renderPage();
  }
}

// 페이지네이션 버튼 업데이트 함수
function updatePaginationButtons() {
  document.getElementById('prevButton').disabled = (currentPage === 1);
  document.getElementById('nextButton').disabled = (currentPage === totalPages);
}

// 페이지 번호 렌더링 함수
function renderPageNumbers() {
  const pageNumbersContainer = document.getElementById('pageNumbers');
  pageNumbersContainer.innerHTML = '';

  if (totalPages <= 1) {
    const pageNumber = document.createElement('span');
    pageNumber.classList.add('page-number', 'active');
    pageNumber.textContent = '1';
    pageNumbersContainer.appendChild(pageNumber);
    return;
  }

  const maxVisiblePages = 5;
  let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
  let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

  if (endPage - startPage + 1 < maxVisiblePages) {
    startPage = Math.max(1, endPage - maxVisiblePages + 1);
  }

  if (startPage > 1) {
    pageNumbersContainer.appendChild(createPageButton(1));
    if (startPage > 2) {
      const ellipsis = document.createElement('span');
      ellipsis.classList.add('ellipsis');
      ellipsis.textContent = '...';
      pageNumbersContainer.appendChild(ellipsis);
    }
  }

  for (let i = startPage; i <= endPage; i++) {
    pageNumbersContainer.appendChild(createPageButton(i));
  }

  if (endPage < totalPages) {
    if (endPage < totalPages - 1) {
      const ellipsis = document.createElement('span');
      ellipsis.classList.add('ellipsis');
      ellipsis.textContent = '...';
      pageNumbersContainer.appendChild(ellipsis);
    }
    pageNumbersContainer.appendChild(createPageButton(totalPages));
  }
}

// 페이지 버튼 생성 함수
function createPageButton(pageNumber) {
  const pageButton = document.createElement('span');
  pageButton.classList.add('page-number');
  if (pageNumber === currentPage) {
    pageButton.classList.add('active');
  }
  pageButton.textContent = pageNumber;
  pageButton.onclick = () => {
    currentPage = pageNumber;
    renderPage();
  };
  return pageButton;
}


// 날짜를 "2024. 7. 31." 형식으로 변환하는 함수
function formatDate(dateString) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = date.getMonth() + 1; // 월은 0부터 시작하므로 +1
  const day = date.getDate();
  return `${year}. ${month}. ${day}.`;
}



// 하트 버튼 클릭 이벤트 리스너 추가 함수
function addHeartButtonListeners() {
  document.querySelectorAll('.heart-button').forEach(button => {
    button.addEventListener('click', function (event) {
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

// 카드 클릭 이벤트 리스너 추가 함수
function addCardClickListeners() {
  document.querySelectorAll('.card').forEach(card => {
    card.addEventListener('click', function () {
      const studyId = this.getAttribute('data-study-id');
      if (studyId) {
        window.location.href = `/study/detail/${studyId}`;
      } else {
        console.error('Study ID is not set for this card.');
      }
    });
  });
}

// 초기 페이지 렌더링 및 이벤트 리스너 설정
document.addEventListener('DOMContentLoaded', function () {
  renderPage();

  const startReviewBtn = document.querySelector('.write-review-button');
  if (startReviewBtn) {
    startReviewBtn.addEventListener('click', function () {
      const url = this.getAttribute('data-url');
      if (url) {
        window.location.href = url;
      } else {
        console.error('Review add URL is not set.');
      }
    });
  }

  addHeartButtonListeners();
});