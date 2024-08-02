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

async function displayComments(questionId) {
    const commentList = document.getElementById('commentList');
    commentList.innerHTML = '';

    try {
        const response = await fetch(`/api/questions/${questionId}/answers`);
        if (!response.ok) {
            throw new Error('Failed to fetch answers');
        }
        const apiResponse = await response.json();

        if (apiResponse.data) {
            comments[questionId] = apiResponse.data.map(answer => ({
                id: answer.id,
                content: answer.content,
                timestamp: new Date(answer.createdAt).getTime(),
                replies: answer.comments || [], // 답변에 대한 댓글을 replies로 저장
            }));

            const sortedComments = comments[questionId].sort((a, b) => b.timestamp - a.timestamp);
            sortedComments.forEach(comment => {
                const commentElement = createCommentElement(comment, questionId);
                commentList.appendChild(commentElement);
            });
        }
    } catch (error) {
        console.error('Error fetching answers:', error);
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
async function addComment() {
    const commentContent = document.getElementById('newComment').value;
    if (commentContent.trim() === '') return;

    try {
        const response = await fetch(`/api/questions/${currentQuestionId}/answers`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            body: JSON.stringify({ content: commentContent })
        });

        if (!response.ok) {
            throw new Error('답변 작성에 실패했습니다.');
        }

        const result = await response.json();
        const newComment = {
            id: result.data.id,
            content: result.data.content,
            replies: [],
            timestamp: new Date(result.data.createdAt).getTime()
        };

        if (!comments[currentQuestionId]) comments[currentQuestionId] = [];
        comments[currentQuestionId].push(newComment);

        document.getElementById('newComment').value = '';
        displayComments(currentQuestionId);
    } catch (error) {
        console.error('Error adding comment:', error);
        alert('댓글 작성 중 오류가 발생했습니다.');
    }
}

function showReplyForm(button, questionId, commentId) {
    const replyForm = button.parentElement.nextElementSibling.nextElementSibling;
    replyForm.style.display = replyForm.style.display === 'none' ? 'block' : 'none';
}

async function addReply(button, questionId, answerId) {
    const replyContent = button.previousElementSibling.value;
    if (replyContent.trim() === '') return;

    try {
        const response = await fetch(`/api/questions/${questionId}/answers/${answerId}/comments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            body: JSON.stringify({ content: replyContent })
        });

        if (response.ok) {
            const data = await response.json();
            const newReply = {
                id: data.data.id,
                content: data.data.content
            };

            const comment = comments[questionId].find(c => c.id === answerId);
            if (!comment.replies) comment.replies = [];
            comment.replies.push(newReply);

            displayComments(questionId); // 댓글 목록을 새로고침하여 새로운 댓글을 표시
            button.previousElementSibling.value = ''; // 입력 필드 초기화
        } else {
            const errorData = await response.json();
            alert("Failed to add reply: " + errorData.error || 'Failed to add reply');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('댓글 추가 중 오류가 발생했습니다.');
    }
}

function editComment(questionId, commentId) {
    const comment = comments[questionId].find(c => c.id === commentId);
    const newContent = prompt("Edit your answer:", comment.content);
    if (newContent !== null && newContent.trim() !== '') {
        fetch(`/api/questions/${questionId}/answers/${commentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            body: JSON.stringify({ content: newContent })
        })
        .then(response => response.json())
        .then(data => {
            if (data.statuscode === "200") {
                comment.content = newContent;
                displayComments(questionId);
            } else {
                alert("Failed to update answer: " + data.msg);
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function deleteComment(questionId, commentId) {
    if (confirm("Are you sure you want to delete this answer?")) {
        fetch(`/api/questions/${questionId}/answers/${commentId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.statuscode === "200") {
                comments[questionId] = comments[questionId].filter(c => c.id !== commentId);
                displayComments(questionId);
            } else {
                alert("Failed to delete answer: " + data.msg);
            }
        })
        .catch(error => console.error('Error:', error));
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

async function editQuestion() {
    const questionId = currentQuestionId;
    const question = questions.find(q => q.id === questionId);

    document.getElementById('questionTitle').value = question.title;
    document.getElementById('selectedCategory').value = question.category;
    document.getElementById('questionContent').value = question.content;

    document.querySelectorAll('.category-option').forEach(option => {
        option.classList.toggle('active', option.dataset.category === question.category);
    });

    const questionModal = document.getElementById("questionModal");
    questionModal.style.display = "block";

    document.getElementById('questionForm').onsubmit = async function(e) {
        e.preventDefault();
        const updatedQuestion = {
            title: document.getElementById('questionTitle').value,
            category: document.getElementById('selectedCategory').value,
            content: document.getElementById('questionContent').value
        };

        try {
            const response = await fetch(`/api/questions/${questionId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify(updatedQuestion)
            });

            if (response.ok) {
                const result = await response.json();
                const updatedQuestionData = result.data;
                const index = questions.findIndex(q => q.id === questionId);
                if (index > -1) {
                    questions[index] = updatedQuestionData;
                }
                showQuestionDetail(questionId);
                questionModal.style.display = "none";
                Swal.fire('Success', 'Question updated successfully', 'success');
            } else {
                const errorData = await response.json();
                Swal.fire('Error', errorData.error || 'Failed to update question', 'error');
            }
        } catch (error) {
            console.error('Error updating question:', error);
            Swal.fire('Error', 'An error occurred while updating the question', 'error');
        }
    };
}

async function deleteQuestion() {
    const questionId = currentQuestionId;

    const result = await Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    });

    if (result.isConfirmed) {
        try {
            const response = await fetch(`/api/questions/${questionId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                }
            });

            if (response.ok) {
                const index = questions.findIndex(q => q.id === questionId);
                if (index > -1) {
                    questions.splice(index, 1);
                }
                showMainPage();
                loadQuestions(currentPage);
                Swal.fire('Deleted!', 'Your question has been deleted.', 'success');
            } else {
                const errorData = await response.json();
                Swal.fire('Error', errorData.error || 'Failed to delete question', 'error');
            }
        } catch (error) {
            console.error('Error deleting question:', error);
            Swal.fire('Error', 'An error occurred while deleting the question', 'error');
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