// 현재 페이지 및 총 페이지 수
let currentPage = 1;
const coursesPerPage = 9; // 페이지당 코스 수
let totalCourses = 0;
let totalPages = 0;
const accessToken = localStorage.getItem('accessToken');

// 페이지 렌더링 함수
function renderPage() {
    fetchCourses().then(courses => {
        const container = document.getElementById('courses-container');
        container.innerHTML = ''; // 기존 콘텐츠를 지웁니다

        // 각 코스를 HTML로 생성하여 컨테이너에 추가합니다
        courses.forEach(course => {
            const courseElement = createCourseElement(course);
            container.appendChild(courseElement);
        });

        // 페이지네이션 버튼 및 페이지 번호 업데이트
        updatePaginationButtons();
        renderPageNumbers();
        addHeartButtonListeners(); // 좋아요 버튼 리스너 추가
    });
}

// API 호출로 코스 데이터를 가져옵니다
function fetchCourses() {
    return fetch(`/api/camps?page=${currentPage - 1}&size=${coursesPerPage}`)
    .then(response => response.json())
    .then(data => {
        if (data.statuscode === "200") {
            totalCourses = data.data.totalElements;
            totalPages = data.data.totalPages;
            return data.data.content; // 코스 목록
        } else {
            console.error('Error:', data.msg);
            return [];
        }
    })
    .catch(error => {
        console.error('API call error:', error);
        return [];
    });
}

// 코스 엘리먼트를 생성합니다
function createCourseElement(course) {
    // 날짜와 시간 포맷팅
    const formatDate = (dateStr) => dateStr ? new Date(dateStr).toLocaleDateString() : '정보 없음';

    // 코스 요소를 생성
    const div = document.createElement('div');
    div.className = 'course';
    div.dataset.id = course.id;

    // imageUrl이 없을 경우 기본 이미지 사용
    const imageUrl = course.imageUrl || 'https://example.com/default-image.jpg';

    // 코스 정보 HTML 구성
    div.innerHTML = `
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
    `;

    // 코스 요소 클릭 시 상세 페이지로 이동
    div.addEventListener('click', () => goToCourseDetail(course.id));

    return div;
}

// 페이지에서 코스 상세보기 함수 예시
function goToCourseDetail(courseId) {
    const url = new URL(`/camp/${courseId}`, window.location.origin);
    window.location.href = url.href;
}


// 페이지네이션 버튼 업데이트 함수
function updatePaginationButtons() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    if (prevBtn) prevBtn.disabled = (currentPage === 1);
    if (nextBtn) nextBtn.disabled = (currentPage === totalPages);
}

// 페이지 번호 렌더링 함수
function renderPageNumbers() {
    const pageNumbersContainer = document.getElementById('pageNumbers');
    if (!pageNumbersContainer) {
        console.error('Page numbers container not found');
        return;
    }

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

// 좋아요 버튼 클릭 시 이벤트 리스너 추가
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


function addReview() {
    if (!accessToken) {
        alert('로그인이 필요합니다.');
        window.location.href = '/auth';
    } else {
        const addUrl = document.querySelector('.write-review-button').getAttribute('review-add-url');
        window.location.href = addUrl;
    }
}

// 페이지 로드 시 코스 데이터를 가져와서 렌더링
document.addEventListener('DOMContentLoaded', function() {
    renderPage();

    // 페이지 버튼이 존재하는 경우에만 이벤트 리스너 추가
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    if (prevBtn) {
        prevBtn.addEventListener('click', function() {
            if (currentPage > 1) {
                currentPage--;
                renderPage();
            }
        });
    } else {
        console.error('prevBtn 버튼이 HTML에 존재하지 않습니다.');
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', function() {
            if (currentPage < totalPages) {
                currentPage++;
                renderPage();
            }
        });
    } else {
        console.error('nextBtn 버튼이 HTML에 존재하지 않습니다.');
    }

    // 기타 버튼 리스너 설정
    const startReviewBtn = document.querySelector('.start-review-btn');
    if (startReviewBtn) {
        startReviewBtn.addEventListener('click', function() {
            const addUrl = this.getAttribute('camp-add');
            window.location.href = addUrl;
        });
    } else {
        console.error('start-review-btn 버튼이 HTML에 존재하지 않습니다.');
    }

    const searchButton = document.getElementById('search-button');
    if (searchButton) {
        searchButton.addEventListener('click', function() {
            const searchTerm = document.getElementById('search-input').value;
            console.log(`Searching for: ${searchTerm}`);
            alert('검색 기능이 실행되었습니다. 실제 구현 시 서버와 통신하여 결과를 표시합니다.');
        });
    } else {
        console.error('search-button 버튼이 HTML에 존재하지 않습니다.');
    }

    const toggleFiltersButton = document.querySelector('.filter-button');
    if (toggleFiltersButton) {
        toggleFiltersButton.addEventListener('click', toggleFilters);
    }

    // 필터 버튼 클릭 시 필터 섹션을 토글
    function toggleFilters() {
        const filterSection = document.getElementById('filter-section');
        const isVisible = filterSection.classList.contains('show');

        // 필터 섹션을 토글
        filterSection.classList.toggle('show');

        // 모든 필터 카테고리 콘텐츠를 숨길 경우
        if (!isVisible) {
            document.querySelectorAll('.filter-category-content').forEach(content => {
                content.style.display = 'none';
            });
        }
    }

    document.querySelectorAll('.filter-category-header').forEach(header => {
        header.addEventListener('click', function() {
            toggleCategory(this);
        });
    });

    // 카테고리 헤더 클릭 시 해당 카테고리 콘텐츠의 표시 상태를 토글
    function toggleCategory(headerElement) {
        const content = headerElement.nextElementSibling;
        const isVisible = content.style.display === 'block';

        // 카테고리 콘텐츠의 표시 상태를 토글
        content.style.display = isVisible ? 'none' : 'block';

        // 토글 아이콘을 업데이트
        const toggleIcon = headerElement.querySelector('.toggle-icon');
        if (toggleIcon) {
            toggleIcon.textContent = isVisible ? '▼' : '▲';
        }
    }


    const resetFiltersButton = document.querySelector('.reset-filters');
    if (resetFiltersButton) {
        resetFiltersButton.addEventListener('click', resetFilters);
    } else {
        console.error('reset-filters 버튼이 HTML에 존재하지 않습니다.');
    }

    function resetFilters() {
        document.querySelectorAll('.filter-option.active').forEach(option => {
            option.classList.remove('active');
        });
    }

    const applyFiltersButton = document.querySelector('.apply-filters');
    if (applyFiltersButton) {
        applyFiltersButton.addEventListener('click', applyFilters);
    } else {
        console.error('apply-filters 버튼이 HTML에 존재하지 않습니다.');
    }

    function applyFilters() {
        const activeFilters = document.querySelectorAll('.filter-option.active');
        const filters = Array.from(activeFilters).map(filter => filter.textContent);
        console.log('Applied filters:', filters);
        alert('필터가 적용되었습니다. 실제 구현 시 서버와 통신하여 결과를 표시합니다.');
    }
});

