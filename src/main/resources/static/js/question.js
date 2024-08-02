const questionsPerPage = 9;
let currentPage = 1;
let totalPages = 0;
const questions = [];
let comments = {};
let currentQuestionId = null;

document.addEventListener('DOMContentLoaded', () => {
    loadQuestions(currentPage);

    const questionModal = document.getElementById("questionModal");
    const questionDetailModal = document.getElementById("questionDetailPage");
    const startBtn = document.querySelector(".start-question-btn");
    const closeBtns = document.querySelectorAll(".close");

    startBtn.onclick = function() {
        questionModal.style.display = "block";
    }

    closeBtns.forEach(btn => {
        btn.onclick = function() {
            questionModal.style.display = "none";
            questionDetailModal.style.display = "none";
        }
    });

    window.onclick = function(event) {
        if (event.target == questionModal) {
            questionModal.style.display = "none";
        } else if (event.target == questionDetailModal) {
            questionDetailModal.style.display = "none";
        }
    }

    document.querySelector('.start-question-btn').addEventListener('click', () => {
        currentQuestionId = null;
        document.getElementById('questionForm').reset();
        document.querySelectorAll('.category-option').forEach(option => option.classList.remove('active'));
        selectedCategory = null;
        questionModal.style.display = "block";
    });

    const categoryOptions = document.querySelectorAll('.category-option');
    categoryOptions.forEach(option => {
        option.addEventListener('click', function() {
            categoryOptions.forEach(opt => opt.classList.remove('active'));
            this.classList.add('active');
            selectedCategory = this.dataset.category;
            document.getElementById('selectedCategory').value = selectedCategory;
        });
    });

    document.getElementById('questionForm').addEventListener('submit', async function(e) {
        e.preventDefault();

        if (!selectedCategory) {
            Swal.fire({
                title: 'Error!',
                text: '카테고리가 선택되지 않았습니다.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        const title = document.getElementById('questionTitle').value;
        const content = document.getElementById('questionContent').value;

        const requestBody = {
            title: title,
            category: selectedCategory,
            content: content
        };

        try {
            const response = await fetch('/api/questions', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify(requestBody)
            });

            if (response.ok) {
                const newQuestion = await response.json();
                questions.unshift(newQuestion);
                Swal.fire({
                    title: '질문 작성 성공',
                    text: '질문이 성공적으로 제출되었습니다!',
                    icon: 'success',
                    confirmButtonText: 'OK'
                }).then(() => {
                    questionModal.style.display = 'none';
                    document.getElementById('questionForm').reset();
                    categoryOptions.forEach(opt => opt.classList.remove('active'));
                    selectedCategory = null;
                    loadQuestions(currentPage);
                });
            } else {
                const errorData = await response.json();
                Swal.fire({
                    title: 'Error!',
                    text: errorData.error || '질문을 제출하는데 문제가 생겼습니다.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        } catch (error) {
            Swal.fire({
                title: 'Error!',
                text: '제출에 문제가 생겼습니다.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    });
    document.querySelector('.back-btn').style.display = 'none';
});

async function loadQuestions(page) {
    try {
        const response = await fetch(`/api/questions?page=${page - 1}&size=${questionsPerPage}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        });


        if (response.ok) {
            const result = await response.json();
            console.log(result.data.content);
            questions.length = 0;
            questions.push(...result.data.content);
            totalPages = result.data.totalPages;
            displayQuestions(page);
            updatePagination();
        } else {
            console.error("Failed to fetch questions:", response.statusText);
            Swal.fire({
                title: 'Error!',
                text: '질문 목록을 불러오는 데 실패했습니다.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    } catch (error) {
        console.error("An error occurred while fetching questions:", error);
        Swal.fire({
            title: 'Error!',
            text: '질문 목록을 불러오는 도중 오류가 발생했습니다.',
            icon: 'error',
            confirmButtonText: 'OK'
        });
    }
}

function displayQuestions() {
    const questionList = document.querySelector('.question-list');
    questionList.innerHTML = '';

    questions.forEach(question => {
        const questionCard = document.createElement('div');
        questionCard.className = 'question-card';
        questionCard.dataset.id = question.id;

        let formattedDate = 'No Date Available';
        if (question.createdAt) {
            const createdAt = new Date(Date.parse(question.createdAt));
            if (!isNaN(createdAt.getTime())) {
                formattedDate = createdAt.toLocaleString();
            }
        }

        questionCard.innerHTML = `
        <div class="question-header">
            <div class="question-title">${question.title}</div>
            <div class="question-meta">
                <span class="question-category">${question.category}</span>
                <span class="question-date">${formattedDate}</span>
            </div>
        </div>
        <div class="question-content">${question.content}</div>
    `;

        questionCard.addEventListener('click', () => showQuestionDetail(question.id));
        document.querySelector('.question-list').appendChild(questionCard);
    });
}
function updatePagination() {
    const pagination = document.querySelector('.pagination');
    pagination.innerHTML = '';

    const prevButton = document.createElement('button');
    prevButton.textContent = 'Previous';
    prevButton.disabled = currentPage === 1;
    prevButton.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            loadQuestions(currentPage);
        }
    });
    pagination.appendChild(prevButton);

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        pageButton.classList.toggle('active', i === currentPage);
        pageButton.addEventListener('click', () => {
            currentPage = i;
            loadQuestions(currentPage);
        });
        pagination.appendChild(pageButton);
    }

    const nextButton = document.createElement('button');
    nextButton.textContent = 'Next';
    nextButton.disabled = currentPage === totalPages;
    nextButton.addEventListener('click', () => {
        if (currentPage < totalPages) {
            currentPage++;
            loadQuestions(currentPage);
        }
    });
    pagination.appendChild(nextButton);
}

function showQuestionDetail(questionId) {
    currentQuestionId = questionId;
    const question = questions.find(r => r.id === questionId);
    const detailPage = document.getElementById('questionDetailPage');
    detailPage.style.display = 'block';
    document.getElementById('detailTitle').textContent = question.title;
    document.getElementById('detailCategory').textContent = `Category: ${question.category}`;
    document.getElementById('detailDate').textContent = `Date: ${question.date}`;
    document.getElementById('detailContent').textContent = question.content;
    displayComments(questionId);
}

function closeDetailModal() {
    const detailPage = document.getElementById('questionDetailPage');
    detailPage.style.display = 'none';
}

function displayComments(questionId) {
    const commentList = document.getElementById('commentList');
    commentList.innerHTML = '';
    if (comments[questionId]) {
        const sortedComments = comments[questionId].sort((a, b) => b.timestamp - a.timestamp);
        sortedComments.forEach(comment => {
            const commentElement = createCommentElement(comment, questionId);
            commentList.appendChild(commentElement);
        });
    }
    updateCommentCount(questionId);
}

function createCommentElement(comment, questionId) {
    const commentElement = document.createElement('div');
    commentElement.className = 'comment';
    commentElement.innerHTML = `
                <p>${comment.content}</p>
                <div class="comment-actions">
                    <button class="edit-btn" onclick="editComment(${questionId}, ${comment.id})">Edit</button>
                    <button class="delete-btn" onclick="deleteComment(${questionId}, ${comment.id})">Delete</button>
                    <button class="reply-button" onclick="showReplyForm(this, ${questionId}, ${comment.id})">Reply</button>
                </div>
                <div class="reply-list"></div>
                <div class="reply-form" style="display: none;">
                    <textarea rows="3" placeholder="대댓글을 작성해주세요"></textarea>
                    <button class="reply-button" onclick="addReply(this, ${questionId}, ${comment.id})">Post Reply</button>
                </div>
            `;
    if (comment.replies) {
        const replyList = commentElement.querySelector('.reply-list');
        comment.replies.forEach(reply => {
            const replyElement = document.createElement('div');
            replyElement.className = 'reply';
            replyElement.innerHTML = `
                        <p>${reply.content}</p>
                        <div class="reply-actions">
                            <button class="edit-btn" onclick="editReply(${questionId}, ${comment.id}, ${reply.id})">Edit</button>
                            <button class="delete-btn" onclick="deleteReply(${questionId}, ${comment.id}, ${reply.id})">Delete</button>
                        </div>
                    `;
            replyList.appendChild(replyElement);
        });
    }
    return commentElement;
}

function addComment() {
    const commentContent = document.getElementById('newComment').value;
    if (commentContent.trim() === '') return;
    if (!comments[currentQuestionId]) comments[currentQuestionId] = [];
    const newComment = {
        id: Date.now(),
        content: commentContent,
        replies: [],
        timestamp: Date.now()
    };
    comments[currentQuestionId].push(newComment);
    document.getElementById('newComment').value = '';
    displayComments(currentQuestionId);
}

function showReplyForm(button, questionId, commentId) {
    const replyForm = button.parentElement.nextElementSibling.nextElementSibling;
    replyForm.style.display = replyForm.style.display === 'none' ? 'block' : 'none';
}

function addReply(button, questionId, commentId) {
    const replyContent = button.previousElementSibling.value;
    if (replyContent.trim() === '') return;
    const comment = comments[questionId].find(c => c.id === commentId);
    if (!comment.replies) comment.replies = [];
    const newReply = {
        id: Date.now(),
        content: replyContent,
        timestamp: Date.now()
    };
    comment.replies.push(newReply);
    displayComments(questionId);
}

function editComment(questionId, commentId) {
    const comment = comments[questionId].find(c => c.id === commentId);
    const newContent = prompt("Edit your comment:", comment.content);
    if (newContent !== null && newContent.trim() !== '') {
        comment.content = newContent;
        displayComments(questionId);
    }
}

function deleteComment(questionId, commentId) {
    if (confirm("Are you sure you want to delete this comment?")) {
        comments[questionId] = comments[questionId].filter(c => c.id !== commentId);
        displayComments(questionId);
    }
}

function editReply(questionId, commentId, replyId) {
    const comment = comments[questionId].find(c => c.id === commentId);
    const reply = comment.replies.find(r => r.id === replyId);
    const newContent = prompt("Edit your reply:", reply.content);
    if (newContent !== null && newContent.trim() !== '') {
        reply.content = newContent;
        displayComments(questionId);
    }
}

function deleteReply(questionId, commentId, replyId) {
    if (confirm("Are you sure you want to delete this reply?")) {
        const comment = comments[questionId].find(c => c.id === commentId);
        comment.replies = comment.replies.filter(r => r.id !== replyId);
        displayComments(questionId);
    }
}

function updateCommentCount(questionId) {
    const commentCountElement = document.getElementById('commentCount');
    let totalCount = 0;
    if (comments[questionId]) {
        totalCount = comments[questionId].length;
        comments[questionId].forEach(comment => {
            if (comment.replies) {
                totalCount += comment.replies.length;
            }
        });
    }
    commentCountElement.textContent = `(${totalCount})`;
}

function showMainPage() {
    document.querySelector('.main-container').style.display = 'flex';
    document.querySelector('.pagination').style.display = 'flex';
    document.getElementById('questionDetailPage').style.display = 'none';
}

function editQuestion() {
    const question = questions.find(r => r.id === currentQuestionId);
    document.getElementById('questionTitle').value = question.title;
    document.getElementById('selectedCategory').value = question.category;
    document.getElementById('questionContent').value = question.content;

    document.querySelectorAll('.category-option').forEach(option => {
        option.classList.toggle('active', option.dataset.category === question.category);
    });

    const questionModal = document.getElementById("questionModal");
    questionModal.style.display = "block";

    document.getElementById('questionForm').onsubmit = function(e) {
        e.preventDefault();
        question.title = document.getElementById('questionTitle').value;
        question.category = document.getElementById('selectedCategory').value;
        question.content = document.getElementById('questionContent').value;
        question.date = new Date().toISOString().split('T')[0];
        showQuestionDetail(currentQuestionId);
        questionModal.style.display = "none";
    };
}

function deleteQuestion() {
    if (confirm("Are you sure you want to delete this question?")) {
        const index = questions.findIndex(r => r.id === currentQuestionId);
        if (index > -1) {
            questions.splice(index, 1);
            delete comments[currentQuestionId];
            showMainPage();
            loadQuestions(currentPage);
        }
    }
}

const categoryButtons = document.querySelectorAll('.category-btn');
categoryButtons.forEach(button => {
    button.addEventListener('click', function() {
        categoryButtons.forEach(btn => btn.classList.remove('active'));
        this.classList.add('active');
        if (this.textContent === '최신순') {
            questions.sort((a, b) => new Date(b.date) - new Date(a.date));
        } else if (this.textContent === '답변 많은 순') {
            questions.sort((a, b) => {
                const commentsA = comments[a.id] ? comments[a.id].length : 0;
                const commentsB = comments[b.id] ? comments[b.id].length : 0;
                return commentsB - commentsA;
            });
        }
        currentPage = 1;
        loadQuestions(currentPage);
    });
});

const categoryOptions = document.querySelectorAll('.category-option');
categoryOptions.forEach(option => {
    option.addEventListener('click', function() {
        categoryOptions.forEach(opt => opt.classList.remove('active'));
        this.classList.add('active');
        document.getElementById('selectedCategory').value = this.dataset.category;
    });
});

loadQuestions(currentPage);
document.querySelector('.back-btn').style.display = 'none';