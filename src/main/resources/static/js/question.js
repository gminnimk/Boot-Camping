const questionsPerPage = 9;
let currentPage = 1;
let totalPages = 0;
const questions = [];
let comments = {};
let currentQuestionId = null;
let mode = 'create';

function openModal(modal, question = null) {
    modal.style.display = "block";

    if (question) {
        mode = 'edit';
        currentQuestionId = question.id;
        document.getElementById('questionTitle').value = question.title;
        document.getElementById('questionContent').value = question.content;
        document.getElementById('selectedCategory').value = question.category;

        document.querySelectorAll('.category-option').forEach(option => {
            option.classList.toggle('active', option.dataset.category === question.category);
        });
    } else {
        mode = 'create';
        resetForm();
    }
}

function closeModals() {
    document.getElementById("questionModal").style.display = "none";
    document.getElementById("questionDetailPage").style.display = "none";
    showMainPage();
}

let selectedCategory = '';
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
    const formElement = document.getElementById('questionForm');
    formElement.removeEventListener('submit', formSubmitHandler);
    formElement.addEventListener('submit', formSubmitHandler);
}

async function formSubmitHandler(e) {
    e.preventDefault();

    if (!selectedCategory) {
        showAlert('오류!', '카테고리가 선택되지 않았습니다.', 'error');
        return;
    }

    const title = document.getElementById('questionTitle').value;
    const content = document.getElementById('questionContent').value;
    const requestBody = { title, category: selectedCategory, content };
    let response;

    try {
        if (mode === 'create') {
            response = await fetch('/api/questions', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify(requestBody)
            });
            window.location.reload();
        } else if (mode === 'edit' && currentQuestionId) {
            response = await fetch(`/api/questions/${currentQuestionId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify(requestBody)
            });
        }

        if (response.ok) {
            closeModals();
            localStorage.setItem('showSuccessMessage', 'true');
        } else {
            const errorData = await response.json();
            showAlert('오류!', errorData.error || '질문을 제출하는데 문제가 생겼습니다.', 'error');
        }
    } catch (error) {
        showAlert('오류!', '질문 제출에 문제가 생겼습니다.', 'error');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM fully loaded and parsed');

    const closeBtns = document.querySelectorAll(".close");
    const startQuestionBtn = document.querySelector('.start-question-btn');
    const questionModal = document.getElementById('questionModal');

    closeBtns.forEach(btn => btn.addEventListener('click', closeModals));

    if (startQuestionBtn && questionModal) {
        startQuestionBtn.addEventListener('click', function(event) {
            event.preventDefault();
            openModal(questionModal);
        });
    } else {
        console.error('Button or modal not found');
    }

    setupCategoryOptions();
    setupFormSubmit();

    if (localStorage.getItem('showSuccessMessage') === 'true') {
        localStorage.removeItem('showSuccessMessage');
        Swal.fire({
            title: '질문 작성 성공',
            text: '질문이 성공적으로 제출되었습니다.',
            icon: 'success',
            confirmButtonText: '확인'
        });
    }

    const categoryButtons = document.querySelectorAll('.category-btn');
    categoryButtons.forEach(button => {
        button.addEventListener('click', function() {
            categoryButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            sortQuestions(this.textContent);
            currentPage = 1;
            loadQuestions(currentPage);
        });
    });

    loadQuestions(currentPage);
    document.querySelector('.back-btn').style.display = 'none';
});

// 질문 정렬 함수
function sortQuestions(sortType) {
    if (sortType === '최신순') {
        questions.sort((a, b) => new Date(b.date) - new Date(a.date));
    } else if (sortType === '답변 많은 순') {
        questions.sort((a, b) => {
            const commentsA = comments[a.id] ? comments[a.id].length : 0;
            const commentsB = comments[b.id] ? comments[b.id].length : 0;
            return commentsB - commentsA;
        });
    }
}

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
            handleError("질문을 가져오는데 싪했습니다.", response.statusText);
        }
    } catch (error) {
        handleError("질문을 가져오는 중 오류가 발생했습니다.", error);
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
    openModal(detailPage, question);
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
        if (!response.ok) throw new Error('답변을 가져오는데 실패했습니다.');

        const apiResponse = await response.json();
        comments[questionId] = await Promise.all(apiResponse.data.map(async answer => {
            const commentResponse = await fetch(`/api/questions/${questionId}/answers/${answer.id}/comments`);
            const commentData = await commentResponse.json();

            return {
                id: answer.id,
                content: answer.content,
                timestamp: new Date(answer.createdAt).getTime(),
                replies: commentData.data.map(reply => ({
                    id: reply.id,
                    content: reply.content,
                    createdAt: reply.createdAt
                })) || []
            };
        }));

        comments[questionId]
            .sort((a, b) => b.timestamp - a.timestamp)
            .forEach(comment => {
                const commentElement = createCommentElement(comment, questionId);
                commentList.appendChild(commentElement);
            });

        updateCommentCount(questionId);
    } catch (error) {
        console.error('답변을 가져오는 중 오류 발생:', error);
    }
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
    if (comment.replies && comment.replies.length > 0) {
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
        handleError('답변 추가 중 오류 발생', error);
    }
}

async function addReply(button, questionId, commentId) {
    const replyContent = button.previousElementSibling.value;
    if (replyContent.trim() === '') return;

    try {
        const response = await fetch(`/api/questions/${questionId}/answers/${commentId}/comments`, {
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
                id: data.data.commentId,
                content: data.data.content
            };

            const comment = comments[questionId].find(c => c.id === commentId);
            if (!comment.replies) comment.replies = [];
            comment.replies.push(newReply);

            displayComments(questionId);
            button.previousElementSibling.value = '';
        } else {
            handleError('댓글 생성을 실패했습니다.', await response.json());
        }
    } catch (error) {
        handleError('댓글 추가 중 오류 발생', error);
    }
}

function editComment(questionId, commentId) {
    const comment = comments[questionId].find(c => c.id === commentId);
    const newContent = prompt("답변을 수정해주세요:", comment.content);
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
                    handleError("답변 업데이트에 실패했습니다.", data);
                }
            })
            .catch(error => handleError('답변 업데이트에 실패했습니다.', error));
    }
}

function deleteComment(questionId, commentId) {
    if (confirm("답변을 정말로 삭제하시겠습니까?")) {
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
                    showAlert("답변 삭제 성공", "답변이 삭제되었습니다.", "success");
                } else {
                    return response.json().then(data => {
                        handleError("답변 삭제에 실패했습니다.", data);
                    });
                }
            })
            .catch(error => handleError('답변 삭제 중 오류 발생', error));
    }
}

function editReply(questionId, answerId, replyId) {
    const answer = comments[questionId].find(c => c.id === answerId);
    const reply = answer.replies.find(r => r.id === replyId);
    const newContent = prompt("Edit your reply:", reply.content);
    if (newContent !== null && newContent.trim() !== '') {
        fetch(`/api/questions/${questionId}/answers/${answerId}/comments/${replyId}`, {
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
                reply.content = newContent;
                displayComments(questionId);
            } else {
                handleError("댓글 업데이트에 실패했습니다.", data);
            }
        })
        .catch(error => handleError('댓글 업데이트 중 오류 발생', error));
    }
}

function deleteReply(questionId, answerId, replyId) {
    if (confirm("댓글을 정말로 삭제하시겠습니까?")){
        if (!replyId) {
            console.error("댓글 ID가 정의되지 않았습니다.");
            showAlert('오류', '댓글ID가 누락되었습니다.', 'error');
            return;
        }
        fetch(`/api/questions/${questionId}/answers/${answerId}/comments/${replyId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        })
        .then(response => {
            if (response.ok) {
                const answer = comments[questionId].find(a => a.id === answerId);
                if (answer) {
                    answer.replies = answer.replies.filter(r => r.id !== replyId);
                }
                displayComments(questionId);
                showAlert("답글 삭제 성공", "답글이 삭제되었습니다.", "success");
            } else {
                return response.json().then(data => {
                    handleError("댓글 삭제에 실패했습니다.", data);
                });
            }
        })
        .catch(error => handleError('댓글 삭제 중 오류 발생', error));
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

    if (question) {
        openModal(document.getElementById("questionModal"), question);
    }

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
                showAlert('질문 수정 성공', '질문이 성공적으로 업데이트되었습니다.', 'success');
                loadQuestions(currentPage);
            } else {
                const errorData = await response.json();
                showAlert('오류', errorData.error || '질문 업데이트에 실패했습니다.', 'error');
            }
        } catch (error) {
            handleError('질문 수정 중 오류 발생', error);
        }
    };
}

async function deleteQuestion() {
    const questionId = currentQuestionId;

    if (confirm("질문을 정말로 삭제하시겠습니까?")) {
        if (!questionId) {
            console.error("질문 ID가 정의되지 않았습니다.");
            showAlert('오류', '질문 ID가 누락되었습니다.', 'error');
            return;
        }
        fetch(`/api/questions/${questionId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        })
        .then(response => {
            if (response.ok) {
                const index = questions.findIndex(q => q.id === questionId);
                if (index > -1) {
                    questions.splice(index, 1);
                }
                showMainPage();
                loadQuestions(currentPage);
                showAlert('삭제 성공', '질문이 삭제되었습니다.', 'success');
            } else {
                return response.json().then(data => {
                    handleError("질문 삭제에 실패했습니다.", data);
                });
            }
        })
        .catch(error => handleError('질문 삭제 중 오류 발생', error));
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
