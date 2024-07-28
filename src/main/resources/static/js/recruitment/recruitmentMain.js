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