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

    // Modal 열기/닫기 핸들러 설정
    startBtn.addEventListener('click', () => openModal(questionModal));
    closeBtns.forEach(btn => btn.addEventListener('click', closeModals));
    window.addEventListener('click', (event) => {
        if (event.target === questionModal || event.target === questionDetailModal) {
            closeModals();
        }
    });

    setupCategoryOptions();
    setupFormSubmit();

    document.querySelector('.back-btn').style.display = 'none';
});

function openModal(modal) {
    modal.style.display = "block";
}

function closeModals() {
    document.getElementById("questionModal").style.display = "none";
    document.getElementById("questionDetailPage").style.display = "none";
    window.location.reload();
}

function setupCategoryOptions() {
    const categoryOptions = document.querySelectorAll('.category-option');
    categoryOptions.forEach(option => {
        option.addEventListener('click', function() {
            categoryOptions.forEach(opt => opt.classList.remove('active'));
            this.classList.add('active');
            selectedCategory = this.dataset.category;
            document.getElementById('selectedCategory').value = selectedCategory;
        });
    });
}

function setupFormSubmit() {
    document.getElementById('questionForm').addEventListener('submit', async function(e) {
        e.preventDefault();

        if (!selectedCategory) {
            showAlert('오류!', '카테고리가 선택되지 않았습니다.', 'error');
            return;
        }

        const title = document.getElementById('questionTitle').value;
        const content = document.getElementById('questionContent').value;
        const requestBody = { title, category: selectedCategory, content };

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
                closeModals();

                localStorage.setItem('showSuccessMessage', 'true');
                window.location.reload();
            } else {
                const errorData = await response.json();
                showAlert('오류!', errorData.error || '질문을 제출하는데 문제가 생겼습니다.', 'error');
            }
        } catch (error) {
            showAlert('오류!', '제출에 문제가 생겼습니다.', 'error');
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    if (localStorage.getItem('showSuccessMessage') === 'true') {
        localStorage.removeItem('showSuccessMessage');
        Swal.fire({
            title: '성공!',
            text: '질문이 성공적으로 제출되었습니다.',
            icon: 'success',
            confirmButtonText: '확인'
        });
    }
});


function resetForm() {
    document.getElementById('questionForm').reset();
    document.querySelectorAll('.category-option').forEach(option => option.classList.remove('active'));
    selectedCategory = null;
}


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
            questions.length = 0;
            questions.push(...result.data.content);
            totalPages = result.data.totalPages;
            displayQuestions();
            updatePagination();
        } else {
            handleError("Failed to fetch questions", response.statusText);
        }
    } catch (error) {
        handleError("An error occurred while fetching questions", error);
    }
}

function displayQuestions() {
    const questionList = document.querySelector('.question-list');
    questionList.innerHTML = '';

    questions.forEach(question => {
        const questionCard = createQuestionCard(question);
        questionList.appendChild(questionCard);
    });
}

function createQuestionCard(question) {
    const questionCard = document.createElement('div');
    questionCard.className = 'question-card';
    questionCard.dataset.id = question.id;

    const formattedDate = question.createdAt ? new Date(Date.parse(question.createdAt)).toLocaleString() : 'No Date Available';

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
    return questionCard;
}

function updatePagination() {
    const pagination = document.querySelector('.pagination');
    pagination.innerHTML = '';

    addPaginationButton(pagination, 'Previous', currentPage === 1, () => {
        if (currentPage > 1) {
            currentPage--;
            loadQuestions(currentPage);
        }
    });

    for (let i = 1; i <= totalPages; i++) {
        addPaginationButton(pagination, i, i === currentPage, () => {
            currentPage = i;
            loadQuestions(currentPage);
        });
    }

    addPaginationButton(pagination, 'Next', currentPage === totalPages, () => {
        if (currentPage < totalPages) {
            currentPage++;
            loadQuestions(currentPage);
        }
    });
}

function addPaginationButton(container, text, isDisabled, onClick) {
    const button = document.createElement('button');
    button.textContent = text;
    button.disabled = isDisabled;
    if (!isDisabled) button.addEventListener('click', onClick);
    container.appendChild(button);
}

function showQuestionDetail(questionId) {
    currentQuestionId = questionId;
    const question = questions.find(q => q.id === questionId);
    const detailPage = document.getElementById('questionDetailPage');
    openModal(detailPage);
    document.getElementById('detailTitle').textContent = question.title;
    document.getElementById('detailCategory').textContent = `Category: ${question.category}`;
    document.getElementById('detailDate').textContent = `Date: ${question.createdAt ? new Date(question.createdAt).toLocaleString() : 'No Date Available'}`;
    document.getElementById('detailContent').textContent = question.content;
    displayComments(questionId);
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
            comments[questionId] = await Promise.all(apiResponse.data.map(async answer => {
                const commentResponse = await fetch(`/api/questions/${questionId}/answers/${answer.id}/comments`);
                const commentData = await commentResponse.json();
                return {
                    id: answer.id,
                    content: answer.content,
                    timestamp: new Date(answer.createdAt).getTime(),
                    replies: commentData.data || []
                };
            }));

            const sortedComments = comments[questionId].sort((a, b) => b.timestamp - a.timestamp);
            sortedComments.forEach(comment => {
                const commentElement = createCommentElement(comment, questionId);
                commentList.appendChild(commentElement);
            });
        }
    } catch (error) {
        console.error('Error fetching answers and comments:', error);
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

    const replyList = commentElement.querySelector('.reply-list');
    if (comment.replies) {
        comment.replies.forEach(reply => {
            const replyElement = document.createElement('div');
            replyElement.className = 'reply';
            replyElement.innerHTML = `
            <p>${reply.content}</p>
            <div class="reply-actions">
                <button class="edit-btn">Edit</button>
                <button class="delete-btn">Delete</button>
            </div>
        `;

            const editBtn = replyElement.querySelector('.edit-btn');
            const deleteBtn = replyElement.querySelector('.delete-btn');

            editBtn.addEventListener('click', () => editReply(questionId, comment.id, reply.id));
            deleteBtn.addEventListener('click', () => deleteReply(questionId, comment.id, reply.id));

            replyList.appendChild(replyElement);
        });
    }

    return commentElement;
}


function showReplyForm(button, questionId, commentId) {
    const replyForm = button.parentElement.nextElementSibling.nextElementSibling;
    replyForm.style.display = replyForm.style.display === 'none' ? 'block' : 'none';
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

        if (!response.ok) throw new Error('답변 작성에 실패했습니다.');

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
        handleError('Error adding comment', error);
    }
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
            handleError('Failed to add reply', await response.json());
        }
    } catch (error) {
        handleError('Error adding reply', error);
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
                handleError("Failed to update answer", data);
            }
        })
        .catch(error => handleError('Error updating comment', error));
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
        .then(response => {
            if (response.ok) {
                comments[questionId] = comments[questionId].filter(c => c.id !== commentId);
                displayComments(questionId);
                showAlert("Success", "답변이 삭제되었습니다.", "success");
            } else {
                return response.json().then(data => {
                    handleError("Failed to delete answer", data);
                });
            }
        })
        .catch(error => handleError('Error deleting comment', error));
    }
}

function editReply(questionId, answerId, commentId) {
    console.log('Edit reply:', questionId, answerId, commentId);
    const comment = comments[answerId]?.find(c => c.id === commentId);
    if (!comment) {
        console.error('Comment not found');
        return;
    }

    const newContent = prompt("Edit your reply:", comment.content);
    if (newContent !== null && newContent.trim() !== '') {
        fetch(`/api/questions/${questionId}/answers/${answerId}/comments/${commentId}`, {
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
                displayComments(answerId);
            } else {
                handleError("Failed to update reply", data);
            }
        })
        .catch(error => handleError('Error updating reply', error));
    }
}

function deleteReply(questionId, answerId, commentId) {
    console.log('Delete reply:', questionId, answerId, commentId);
    if (confirm("Are you sure you want to delete this reply?")) {
        fetch(`/api/questions/${questionId}/answers/${answerId}/comments/${commentId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        })
        .then(response => {
            if (response.ok) {
                comments[answerId] = comments[answerId].filter(c => c.id !== commentId);
                displayComments(answerId);
                showAlert("Success", "답글이 삭제되었습니다.", "success");
            } else {
                return response.json().then(data => {
                    handleError("Failed to delete reply", data);
                });
            }
        })
        .catch(error => handleError('Error deleting reply', error));
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

    openModal(document.getElementById("questionModal"));

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
                closeModals();
                showAlert('Success', 'Question updated successfully', 'success');
            } else {
                const errorData = await response.json();
                showAlert('Error', errorData.error || 'Failed to update question', 'error');
            }
        } catch (error) {
            handleError('Error updating question', error);
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
                showAlert('Deleted!', 'Your question has been deleted.', 'success');
            } else {
                const errorData = await response.json();
                showAlert('Error', errorData.error || 'Failed to delete question', 'error');
            }
        } catch (error) {
            handleError('Error deleting question', error);
        }
    }
}

function showAlert(title, text, icon, callback = null) {
    Swal.fire({
        title,
        text,
        icon,
        confirmButtonText: 'OK'
    }).then(() => {
        if (callback) callback();
    });
}

function handleError(message, error) {
    console.error(message, error);
    showAlert('Error!', message, 'error');
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

loadQuestions(currentPage);
document.querySelector('.back-btn').style.display = 'none';
