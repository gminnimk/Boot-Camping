const coursesPerPage = 9;  // 한 페이지당 코스 개수
let currentPage = 1;
let totalCourses = 0;
let totalPages = 0;
const accessToken = localStorage.getItem('accessToken');
const ITEMS_PER_PAGE = 9;

// API에서 코스 데이터를 가져오는 함수
function fetchCourses() {
    return fetch(`/api/camps?page=${currentPage - 1}&size=${coursesPerPage}`)
    .then(response => response.json())
    .then(data => {
        if (data.statuscode === "200") {
            totalCourses = data.data.totalElements;
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

// 날짜를 "2024. 7. 31." 형식으로 변환하는 함수
function formatDate(dateString) {
    if (!dateString) return '정보 없음';
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${year}. ${month}. ${day}.`;
}

// 코스 카드 생성 함수
function createCourseCard(course) {
    const imageUrl = course.imageUrl || 'https://example.com/default-image.jpg';
    return `
        <div class="course" data-id="${course.id}" onclick="goToCourseDetail(${course.id})">
            <div class="course-image" style="background-image: url('${imageUrl}');"></div>
            <div class="course-info">
                <div class="course-header">
                    <h3>${course.title || '제목 없음'}</h3>
                    <div class="heart-container">
                        <button class="heart-button" data-likes="${course.likes || 0}">
                            <i class="far fa-heart"></i>
                        </button>
                        <div class="like-count">${course.likes || 0}</div>
                    </div>
                </div>
                <div class="course-categories">
                    <span class="category">${course.trek || '정보 없음'}</span>
                    <span class="category">${course.cost || '정보 없음'}</span>
                </div>
                <p class="course-institution">운영기관 : ${course.campName || '정보 없음'}</p>
                <p>모집 기간 : ${formatDate(course.recruitStart)} ~ ${formatDate(course.recruitEnd)}</p>
                <p>참여 기간 : ${formatDate(course.campStart)} ~ ${formatDate(course.campEnd)}</p>
            </div>
        </div>
    `;
}

function goToCourseDetail(courseId) {
    const url = new URL(`/camp/${courseId}`, window.location.origin);
    window.location.href = url.href;
}

// 페이지 렌더링 함수
function renderPage() {
    fetchCourses().then(courses => {
        const container = document.getElementById('courses-container');
        container.innerHTML = '';
        courses.forEach(course => {
            container.innerHTML += createCourseCard(course);
        });
        updatePaginationButtons();
        renderPageNumbers();
        addHeartButtonListeners();
    });
}

// 페이지 변경 함수
function changePage(direction) {
    currentPage += direction;
    renderPage();
}

// 페이지네이션 버튼 업데이트 함수
function updatePaginationButtons() {
    document.getElementById('prevBtn').disabled = (currentPage === 1);
    document.getElementById('nextBtn').disabled = (currentPage === totalPages);
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

function addHeartButtonListeners() {
    document.querySelectorAll('.heart-button').forEach(button => {
        button.addEventListener('click', function(event) {
            event.stopPropagation();
            const icon = this.querySelector('i');
            const likeCountElement = this.nextElementSibling;
            let likes = parseInt(this.getAttribute('data-likes'));

            if (icon.classList.contains('far')) {
                icon.classList.remove('far');
                icon.classList.add('fas');
                likes++;
            } else {
                icon.classList.remove('fas');
                icon.classList.add('far');
                likes--;
            }

            this.setAttribute('data-likes', likes);
            likeCountElement.textContent = likes;
        });
    });
}

function addCamp() {
    if (!accessToken) {
        alert('로그인이 필요합니다.');
        window.location.href = '/auth';
    } else {
        const addUrl = document.querySelector('.start-review-btn').getAttribute('camp-add');
        window.location.href = addUrl;
    }
}

// 초기 페이지 렌더링 및 이벤트 리스너 설정
window.addEventListener('load', function() {
    var allElements = document.getElementsByTagName('*');
    Array.prototype.forEach.call(allElements, function(el) {
        var includePath = el.dataset.includePath;
        if (includePath) {
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    el.outerHTML = this.responseText;
                }
            };
            xhttp.open('GET', includePath, true);
            xhttp.send();
        }
    });

    renderPage();  // 초기 페이지 렌더링

    // 이벤트 리스너 설정
    document.getElementById('prevBtn').addEventListener('click', () => changePage(-1));
    document.getElementById('nextBtn').addEventListener('click', () => changePage(1));

    const startReviewBtn = document.querySelector('.start-review-btn');
    if (startReviewBtn) {
        startReviewBtn.addEventListener('click', addCamp);
    }

    // 정렬 버튼 이벤트 리스너
    document.querySelectorAll('.sort-button').forEach(button => {
        button.addEventListener('click', function() {
            const sortType = this.dataset.sort;
            console.log(`정렬 타입: ${sortType}`);
            // 여기에 정렬 로직 구현
        });
    });
});


// 현재 페이지에 맞는 결과만 업데이트합니다.
function updateResultsForCurrentPage() {
    const resultsContainer = document.getElementById('resultsContainer');
    resultsContainer.innerHTML = ''; // 기존 결과를 지웁니다.

    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const endIndex = Math.min(startIndex + ITEMS_PER_PAGE, allResults.length);

    for (let i = startIndex; i < endIndex; i++) {
        const rank = allResults[i];
        const recruitment = rank.recruitment;
        const track = recruitment.track;
        const environment = recruitment.place;
        const cost = recruitment.cost;
        const name = recruitment.name;

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
       "${recruitment.process}"
      </div>
      <div class="review-meta">
        작성자: John D. | 날짜: 2023-05-15
      </div>
    `;

        resultsContainer.appendChild(resultItem);
    }

    addHeartButtonListeners();
    renderPage(); // 페이지네이션 업데이트
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

document.addEventListener('DOMContentLoaded', addHeartButtonListeners);