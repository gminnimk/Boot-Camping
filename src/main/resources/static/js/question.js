const questionsPerPage = 9;
let currentPage = 1;
const questions = [
    {
        id: 1,
        title: "강의 내용이 매우 유익해요",
        category: "강의",
        date: "2023-04-15",
        content: "강의 내용이 정말 알찼습니다. 실무에서 바로 적용할 수 있는 내용들이 많아 좋았어요."
    },
    {
        id: 2,
        title: "강사님의 열정적인 강의",
        category: "강사",
        date: "2023-04-14",
        content: "강사님의 열정이 대단했습니다. 학생들의 질문에 항상 친절하게 답변해주셔서 좋았어요."
    },
    {
        id: 3,
        title: "멋진 캠핑 여행 경험",
        category: "캠핑여행",
        date: "2023-04-13",
        content: "캠핑 여행이 정말 좋았어요. 자연 속에서 동기들과 함께 시간을 보내며 많은 것을 배웠습니다."
    },
    {
        id: 4,
        title: "체계적인 커리큘럼",
        category: "커리큘럼",
        date: "2023-04-12",
        content: "커리큘럼이 매우 체계적으로 구성되어 있어 단계별로 학습하기 좋았습니다."
    },
    {
        id: 5,
        title: "쾌적한 수강 환경",
        category: "수강환경",
        date: "2023-04-11",
        content: "강의실 환경이 쾌적하고 시설이 잘 갖춰져 있어 공부하기 좋았습니다."
    },
    {
        id: 6,
        title: "도전적이고 유익한 과제들",
        category: "과제",
        date: "2023-04-10",
        content: "과제가 도전적이면서도 실력 향상에 큰 도움이 되었습니다. 과제를 통해 많은 것을 배웠어요."
    },
    {
        id: 7,
        title: "실무와 유사한 프로젝트 경험",
        category: "프로젝트",
        date: "2023-04-09",
        content: "프로젝트를 통해 실무와 유사한 경험을 할 수 있어 좋았습니다. 팀워크도 배울 수 있었어요."
    },
    {
        id: 8,
        title: "취업 연계 프로그램이 도움 돼요",
        category: "취업연계",
        date: "2023-04-08",
        content: "취업 연계 프로그램을 통해 실제 취업에 많은 도움을 받았습니다. 멘토링 세션이 특히 유익했어요."
    },
    {
        id: 9,
        title: "강의 내용의 깊이가 있어요",
        category: "강의",
        date: "2023-04-07",
        content: "강의 내용이 깊이가 있어 좋았습니다. 기초부터 고급 내용까지 잘 다뤄주셔서 만족스러웠어요."
    },
    {
        id: 10,
        title: "강사님의 실무 경험 공유",
        category: "강사",
        date: "2023-04-06",
        content: "강사님의 풍부한 실무 경험을 바탕으로 한 강의가 매우 인상적이었습니다."
    },
    {
        id: 11,
        title: "캠핑 여행에서의 팀 빌딩",
        category: "캠핑여행",
        date: "2023-04-05",
        content: "캠핑 여행을 통해 동기들과 더 가까워질 수 있었고, 팀워크를 기를 수 있어 좋았습니다."
    },
    {
        id: 12,
        title: "유연한 커리큘럼 운영",
        category: "커리큘럼",
        date: "2023-04-04",
        content: "커리큘럼이 유연하게 운영되어 학생들의 요구사항을 잘 반영해주셔서 좋았습니다."
    }
];

let comments = {};
let currentQuestionId = null;

function displayQuestions(page) {
    const questionList = document.querySelector('.question-list');
    questionList.innerHTML = '';
    const start = (page - 1) * questionsPerPage;
    const end = start + questionsPerPage;
    const pageQuestions = questions.slice(start, end);

    pageQuestions.forEach(question => {
        const questionCard = document.createElement('div');
        questionCard.className = 'question-card';
        questionCard.dataset.id = question.id;
        questionCard.innerHTML = `
                    <div class="question-header">
                        <div class="question-title">${question.title}</div>
                        <div class="question-meta">
                            <span class="question-category">${question.category}</span>
                            <span class="question-date">${question.date}</span>
                        </div>
                    </div>
                    <div class="question-content">${question.content}</div>
                `;
        questionCard.addEventListener('click', () => showQuestionDetail(question.id));
        questionList.appendChild(questionCard);
    });

    updatePagination();
}

function updatePagination() {
    const pagination = document.querySelector('.pagination');
    pagination.innerHTML = '';
    const totalPages = Math.ceil(questions.length / questionsPerPage);

    const prevButton = document.createElement('button');
    prevButton.textContent = 'Previous';
    prevButton.disabled = currentPage === 1;
    prevButton.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            displayQuestions(currentPage);
        }
    });
    pagination.appendChild(prevButton);

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        pageButton.classList.toggle('active', i === currentPage);
        pageButton.addEventListener('click', () => {
            currentPage = i;
            displayQuestions(currentPage);
        });
        pagination.appendChild(pageButton);
    }

    const nextButton = document.createElement('button');
    nextButton.textContent = 'Next';
    nextButton.disabled = currentPage === totalPages;
    nextButton.addEventListener('click', () => {
        if (currentPage < totalPages) {
            currentPage++;
            displayQuestions(currentPage);
        }
    });
    pagination.appendChild(nextButton);
}

function showQuestionDetail(questionId) {
    currentQuestionId = questionId;
    const question = questions.find(r => r.id === questionId);
    document.querySelector('.main-container').style.display = 'none';
    document.querySelector('.pagination').style.display = 'none';
    const detailPage = document.getElementById('questionDetailPage');
    detailPage.style.display = 'block';
    document.getElementById('detailTitle').textContent = question.title;
    document.getElementById('detailCategory').textContent = `Category: ${question.category}`;
    document.getElementById('detailDate').textContent = `Date: ${question.date}`;
    document.getElementById('detailContent').textContent = question.content;
    displayComments(questionId);
    document.querySelector('.back-btn').style.display = 'block';
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
    document.getElementById('writeQuestionPage').style.display = 'none';
    document.querySelector('.back-btn').style.display = 'none';
}

function showWriteQuestionPage() {
    document.querySelector('.main-container').style.display = 'none';
    document.querySelector('.pagination').style.display = 'none';
    document.getElementById('questionDetailPage').style.display = 'none';
    document.getElementById('writeQuestionPage').style.display = 'block';
    document.querySelector('.back-btn').style.display = 'block';
}

function editQuestion() {
    const question = questions.find(r => r.id === currentQuestionId);
    document.getElementById('questionTitle').value = question.title;
    document.getElementById('selectedCategory').value = question.category;
    document.getElementById('questionContent').value = question.content;

    // Highlight the correct category option
    document.querySelectorAll('.category-option').forEach(option => {
        option.classList.toggle('active', option.dataset.category === question.category);
    });

    showWriteQuestionPage();
    document.getElementById('questionForm').onsubmit = function(e) {
        e.preventDefault();
        question.title = document.getElementById('questionTitle').value;
        question.category = document.getElementById('selectedCategory').value;
        question.content = document.getElementById('questionContent').value;
        question.date = new Date().toISOString().split('T')[0]; // Update the date
        showQuestionDetail(currentQuestionId);
    };
}

function deleteQuestion() {
    if (confirm("Are you sure you want to delete this question?")) {
        const index = questions.findIndex(r => r.id === currentQuestionId);
        if (index > -1) {
            questions.splice(index, 1);
            delete comments[currentQuestionId];
            showMainPage();
            displayQuestions(currentPage);
        }
    }
}

document.querySelector('.start-question-btn').addEventListener('click', () => {
    currentQuestionId = null; // Reset currentQuestionId for new question
    document.getElementById('questionForm').reset(); // Clear the form
    document.querySelectorAll('.category-option').forEach(option => option.classList.remove('active'));
    showQuestionDetailPage();
});

function showQuestionDetailPage() {
    document.querySelector('.main-container').style.display = 'none';
    document.querySelector('.pagination').style.display = 'none';
    const detailPage = document.getElementById('questionDetailPage');
    detailPage.style.display = 'block';

    document.getElementById('detailTitle').textContent = '새 질문 작성';
    document.getElementById('detailCategory').textContent = '';
    document.getElementById('detailDate').textContent = new Date().toISOString().split('T')[0];
    document.getElementById('detailContent').textContent = '질문을 입력하세요.';

    document.querySelector('.back-btn').style.display = 'block';
}

document.getElementById('questionForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const title = document.getElementById('questionTitle').value;
    const category = document.getElementById('selectedCategory').value;
    const content = document.getElementById('questionContent').value;
    if (currentQuestionId === null) {
        // This is a new question
        const newQuestion = {
            id: questions.length + 1,
            title: title,
            category: category,
            date: new Date().toISOString().split('T')[0],
            content: content
        };
        questions.unshift(newQuestion);
    } else {
        // This is an edit to an existing question
        const question = questions.find(r => r.id === currentQuestionId);
        question.title = title;
        question.category = category;
        question.content = content;
        question.date = new Date().toISOString().split('T')[0]; // Update the date
    }
    showMainPage();
    displayQuestions(1);
});

const categoryButtons = document.querySelectorAll('.category-btn');
categoryButtons.forEach(button => {
    button.addEventListener('click', function() {
        categoryButtons.forEach(btn => btn.classList.remove('active'));
        this.classList.add('active');
        console.log('Category selected:', this.textContent);
        // Here you can add logic to filter questions by the selected sorting method
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
        displayQuestions(currentPage);
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

// Initial display
displayQuestions(currentPage);
document.querySelector('.back-btn').style.display = 'none';
