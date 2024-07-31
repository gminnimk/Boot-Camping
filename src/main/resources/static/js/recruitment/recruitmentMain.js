function startReview() {
    const addUrl = document.querySelector('.start-review-btn').getAttribute('camp-add');
    window.location.href = addUrl;
}

function searchCourses() {
    const searchTerm = document.getElementById('search-input').value;
    console.log(`Searching for: ${searchTerm}`);
    alert('검색 기능이 실행되었습니다. 실제 구현 시 서버와 통신하여 결과를 표시합니다.');
}

function toggleFilters() {
    const filterSection = document.getElementById('filter-section');
    filterSection.classList.toggle('show');
}

function toggleCategory(header) {
    const content = header.nextElementSibling;
    const icon = header.querySelector('.toggle-icon');
    content.classList.toggle('show');
    icon.textContent = content.classList.contains('show') ? '▲' : '▼';
}

function resetFilters() {
    document.querySelectorAll('.filter-option.active').forEach(option => {
        option.classList.remove('active');
    });
}

function applyFilters() {
    const activeFilters = document.querySelectorAll('.filter-option.active');
    const filters = Array.from(activeFilters).map(filter => filter.textContent);
    console.log('Applied filters:', filters);
    alert('필터가 적용되었습니다. 실제 구현 시 서버와 통신하여 결과를 표시합니다.');
}

let currentPage = 1;
const totalPages = 4;

function updatePagination() {
    document.getElementById('prevBtn').disabled = (currentPage === 1);
    document.getElementById('nextBtn').disabled = (currentPage === totalPages);

    document.querySelectorAll('.pagination button:not(#prevBtn):not(#nextBtn)').forEach((btn, index) => {
        btn.classList.toggle('active', index + 1 === currentPage);
    });
}

document.getElementById('prevBtn').addEventListener('click', function() {
    if (currentPage > 1) {
        currentPage--;
        updatePagination();
    }
});

document.getElementById('nextBtn').addEventListener('click', function() {
    if (currentPage < totalPages) {
        currentPage++;
        updatePagination();
    }
});

document.querySelectorAll('.pagination button:not(#prevBtn):not(#nextBtn)').forEach(btn => {
    btn.addEventListener('click', function() {
        currentPage = parseInt(this.textContent);
        updatePagination();
    });
});

// Add click event listeners to course cards
document.querySelectorAll('.course').forEach(course => {
    course.addEventListener('click', function(event) {
        if (!event.target.closest('.heart-button')) {
            const courseId = this.getAttribute('data-id');
            window.location.href = `https://bootcamp.example.com/course/${courseId}`;
        }
    });
});

// Add click event listeners to heart buttons
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

// Add click event listeners to sort buttons
document.querySelectorAll('.sort-button').forEach(button => {
    button.addEventListener('click', function() {
        document.querySelectorAll('.sort-button').forEach(btn => btn.classList.remove('active'));
        this.classList.add('active');
        const sortType = this.getAttribute('data-sort');
        console.log(`Sorting by: ${sortType}`);
    });
});

// Add click event listeners to filter options
document.querySelectorAll('.filter-option').forEach(option => {
    option.addEventListener('click', function() {
        this.classList.toggle('active');
    });
});

document.addEventListener("DOMContentLoaded", function() {
    fetchCourses(0); // 초기 페이지 번호 0으로 시작
});

async function fetchCourses(page) {
    try {
        const response = await fetch(`/api/camps?page=${page}`);
        const result = await response.json();

        if (response.ok) {
            displayCourses(result.data.content);
            setupPagination(result.data);
        } else {
            console.error('데이터 가져오기 실패:', result);
        }
    } catch (error) {
        console.error('코스 데이터를 가져오는 중 오류 발생:', error);
    }
}


function displayCourses(courses) {
    const container = document.getElementById('courses-container');
    container.innerHTML = ''; // 기존 콘텐츠를 지웁니다

    courses.forEach(course => {
        const courseElement = `
            <div class="course" data-id="${course.id}">
                <div class="course-image" style="background-image: url('${course.imageUrl || 'default-image.jpg'}');"></div>
                <div class="course-info">
                    <div class="course-header">
                        <h3>${course.title}</h3>
                        <div class="heart-container">
                            <button class="heart-button" data-likes="${course.likes}">
                                <i class="far fa-heart"></i>
                            </button>
                            <div class="like-count">${course.likes}</div>
                        </div>
                    </div>
                    <div class="course-categories">
                        <span class="category">${course.process}</span>
                        <span class="category">${course.trek}</span>
                    </div>
                    <p class="course-institution">운영기관: ${course.place}</p>
                    <p>모집 기간: ${course.recruitStart} ~ ${course.recruitEnd}</p>
                    <p>참여 기간: ${course.campStart} ~ ${course.campEnd}</p>
                </div>
            </div>
        `;
        container.innerHTML += courseElement;
    });
}

function setupPagination(paginationData) {
    const paginationContainer = document.getElementById('pagination-container');
    paginationContainer.innerHTML = ''; // 기존 페이지 네비게이션을 지웁니다

    if (paginationData.totalPages > 1) {
        for (let i = 0; i < paginationData.totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i + 1;
            pageButton.className = i === paginationData.pageable.pageNumber ? 'active' : '';
            pageButton.addEventListener('click', () => fetchCourses(i));
            paginationContainer.appendChild(pageButton);
        }
    }
}
