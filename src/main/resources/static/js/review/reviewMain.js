const reviewsPerPage = 9;  // 한 페이지당 개수
let currentPage = 1; 
const totalReviews = 12; // 총 리뷰 개수
const totalPages = Math.ceil(totalReviews / reviewsPerPage);

const reviews = [
    {title: "Breathtaking Views at Sunset Camp", category1: "Mountain", category2: "Adventure", date: "2023-04-15", stars: 5, content: "Absolutely breathtaking views, highly recommend this spot for camping.", id: 1, author: "Sarah M."},
    {title: "Friendly Staff at Pine Valley Campground", category1: "Forest", category2: "Family", date: "2023-04-14", stars: 5, content: "Great facilities and friendly staff, had an amazing time camping here. The surrounding hiking trails were also fantastic.", id: 2, author: "Mike T."},
    {title: "Perfect Weekend Getaway at Lakeside Retreat", category1: "Lake", category2: "Relaxation", date: "2023-04-13", stars: 5, content: "Perfect location for a weekend escape, will definitely come back. The lake was beautiful and perfect for swimming and kayaking.", id: 3, author: "Emily L."},
    {title: "Beautiful but Buggy at Mosquito Creek Campground", category1: "River", category2: "Nature", date: "2023-04-12", stars: 4, content: "Beautiful campsite with great amenities. Only downside was the mosquitoes. Make sure to bring plenty of bug spray!", id: 4, author: "David R."},
    {title: "Stargazing Paradise at Mountain View Camp", category1: "Mountain", category2: "Romantic", date: "2023-04-11", stars: 5, content: "Fantastic experience! The stargazing at night was absolutely magical. The campsite was quiet and peaceful, perfect for relaxation.", id: 5, author: "Lisa K."},
    {title: "Great Hiking at Forest Edge Campsite", category1: "Forest", category2: "Adventure", date: "2023-04-10", stars: 4, content: "Great hiking trails nearby. Campsite was clean and well-maintained. The only improvement could be more showers.", id: 6, author: "Tom W."},
    {title: "Cozy Cabins at Pinecone Resort", category1: "Resort", category2: "Luxury", date: "2023-04-09", stars: 5, content: "Stayed in one of the cozy cabins and it was a delightful experience. The amenities were top-notch and the staff was incredibly helpful.", id: 7, author: "Anna S."},
    {title: "Adventure-filled Stay at River Rapids Campground", category1: "River", category2: "Thrilling", date: "2023-04-08", stars: 4, content: "If you're looking for adventure, this is the place! Great rafting nearby. The campground itself was decent, but the location is unbeatable.", id: 8, author: "Mark J."},
    {title: "Peaceful Retreat at Whispering Pines", category1: "Forest", category2: "Relaxation", date: "2023-04-07", stars: 5, content: "Such a tranquil spot! The pine forest creates a serene atmosphere. Perfect for those seeking a quiet getaway from the city hustle.", id: 9, author: "Sophie L."},
    {title: "Family Fun at Sunshine Meadows Campsite", category1: "Meadow", category2: "Family", date: "2023-04-06", stars: 4, content: "Great family-friendly campsite with lots of activities for kids. The playground and mini-golf were big hits. Could use more shade in some areas.", id: 10, author: "John D."},
    {title: "Rustic Experience at Bear Creek Wilderness", category1: "Wilderness", category2: "Nature", date: "2023-04-05", stars: 3, content: "If you're looking for a true wilderness experience, this is it. Very basic facilities, but the surrounding nature is stunning. Not for the faint-hearted!", id: 11, author: "Emma R."},
    {title: "Luxurious Glamping at Evergreen Retreat", category1: "Glamping", category2: "Luxury", date: "2023-04-04", stars: 5, content: "For those who want to camp in style, this glamping site is perfect. The tents are spacious and well-equipped. The on-site restaurant serves delicious meals.", id: 12, author: "Alex M."}
];

// 리뷰 카드 생성 함수
function createReviewCard(review) {
    return `
        <div class="review-card" onclick="goToReviewDetail(${review.id})">
            <div class="review-header">
                <div class="reviews review-title">${review.title}</div>
                <div class="reviews heart-icon heart-button"><i class="far fa-heart"></i></div>
            </div>
            <div class="reviewer-info">
                <span class="reviews review-category">${review.category1}</span>
                <span class="reviews review-category">${review.category2}</span>
                <span class="reviews review-date">${review.date}</span>
            </div>
            <div class="review-stars">${'★'.repeat(review.stars)}${'☆'.repeat(5-review.stars)}</div>
            <p class="reviews review-content">${review.content}</p>
            <span class="reviews review-author">- ${review.author}</span>
        </div>
    `;
}

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
    renderPage();  // 초기 페이지 렌더링 추가
});

// 페이지 렌더링 함수
function renderPage() {
    const container = document.getElementById('reviewContainer');
    container.innerHTML = '';
    const start = (currentPage - 1) * reviewsPerPage;
    const end = start + reviewsPerPage;
    const pageReviews = reviews.slice(start, end);
    pageReviews.forEach(review => {
        container.innerHTML += createReviewCard(review);
    });
    updatePaginationButtons();
    renderPageNumbers();
    addHeartButtonListeners();
}

// 페이지 변경 함수
function changePage(direction) {
    currentPage += direction;
    renderPage();
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

function addHeartButtonListeners() {
    document.querySelectorAll('.heart-button').forEach(button => {
        button.addEventListener('click', function(event) {
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


function addReview() {
    const addUrl = document.querySelector('.write-review-button').getAttribute('review-add-url');
    window.location.href = addUrl;
}

// 초기 페이지 렌더링
renderPage();
document.addEventListener('DOMContentLoaded', addHeartButtonListeners);