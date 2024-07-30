document.addEventListener("DOMContentLoaded", function() {
    fetchProfiles();

    const profileStatus = localStorage.getItem('profileStatus');
    if (profileStatus) {
        Swal.fire({
            toast: true,
            position: 'center',
            icon: 'success',
            title: profileStatus === 'edited' ? '프로필이 수정되었습니다.' : '프로필이 등록되었습니다.',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer);
                toast.addEventListener('mouseleave', Swal.resumeTimer);
            },
            customClass: {
                title: 'black-text'
            }
        });
        localStorage.removeItem('profileStatus');
    }
});

const modal = document.getElementById("bootcampModal");
const activityModal = document.getElementById("activityModal");
const registerBtn = document.getElementById("openModal");
const span = document.getElementsByClassName("close")[0];
const activitySpan = document.getElementsByClassName("close")[1];
const form = document.getElementById("bootcampForm");
const techStackButtons = document.querySelectorAll(".tech-stack-button");
const modalTitle = document.getElementById("modalTitle");
const submitButton = document.getElementById("submitButton");
const bootcampNameSelect = document.getElementById("bootcampName");
const trackSelect = document.getElementById("track");
const activityCards = document.querySelectorAll(".activity-card");
const activityModalTitle = document.getElementById("activityModalTitle");
const activityList = document.getElementById("activityList");
const bootcampCertificate = document.getElementById("bootcampCertificate");
const certificatePreview = document.getElementById("certificatePreview");
const certificateGroup = document.getElementById("certificateGroup");

let isEditing = false;
let currentEditingProfileId = null;

const accessToken = localStorage.getItem('accessToken');

if (!accessToken) {
    alert('로그인이 필요합니다.');
    window.location.href = '/auth';
}

registerBtn.onclick = function() {
    isEditing = false;
    modalTitle.textContent = "부트캠프 등록";
    submitButton.textContent = "등록";
    bootcampNameSelect.disabled = false;
    trackSelect.disabled = false;
    form.reset();
    techStackButtons.forEach(button => button.classList.remove("selected"));
    certificatePreview.style.display = "none";
    certificateGroup.style.display = "block";
    modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

activitySpan.onclick = function() {
    activityModal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
    if (event.target === activityModal) {
        activityModal.style.display = "none";
    }
}

techStackButtons.forEach(button => {
    button.addEventListener("click", function() {
        this.classList.toggle("selected");
    });
});

bootcampCertificate.addEventListener("change", function() {
    const file = this.files[0];
    if (file) {
        if (file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = function(e) {
                certificatePreview.src = e.target.result;
                certificatePreview.style.display = "block";
            }
            reader.readAsDataURL(file);
        } else if (file.type === 'application/pdf') {
            certificatePreview.src = "pdf_icon.png"; // PDF 아이콘 이미지 경로
            certificatePreview.style.display = "block";
        }
    }
});

form.addEventListener("submit", function(e) {
    e.preventDefault();
    const bootcampName = document.getElementById("bootcampName").value;
    const track = document.getElementById("track").value;
    const generation = document.getElementById("generation").value;
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const selectedTechStack = Array.from(document.querySelectorAll(".tech-stack-button.selected")).map(button => button.textContent);
    const certificateFile = bootcampCertificate.files[0];

    const profileData = {
        bootcampName,
        track,
        generation,
        startDate,
        endDate,
        techStack: selectedTechStack,
        certificate: certificateFile ? certificateFile.name : null
    };

    let apiUrl = "/api/profiles";
    let method = "POST";

    if (isEditing) {
        apiUrl = `/api/profiles/${currentEditingProfileId}`;
        method = "PUT";
    }

    fetch(apiUrl, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + accessToken
        },
        body: JSON.stringify(profileData)
    })
        .then(response => response.text())
        .then(data => {
            console.log("성공:", data);
            if (isEditing) {
                updateCardInDOM(currentEditingProfileId, profileData);
                localStorage.setItem('profileStatus', 'edited');
            } else {
                addCardToDOM(data);
                localStorage.setItem('profileStatus', 'registered');
            }
            window.location.reload(); // 페이지 새로고침
        })
        .catch((error) => {
            console.error("에러:", error);
        });
});

document.addEventListener("click", function(e) {
    if (e.target && e.target.classList.contains("edit-button")) {
        isEditing = true;
        currentEditingProfileId = e.target.dataset.profileId;
        modalTitle.textContent = "부트캠프 정보 수정";
        submitButton.textContent = "수정";

        bootcampNameSelect.value = e.target.dataset.bootcamp;
        bootcampNameSelect.disabled = true;
        trackSelect.value = e.target.dataset.track;
        trackSelect.disabled = true;
        document.getElementById("generation").value = e.target.dataset.generation;
        document.getElementById("startDate").value = e.target.dataset.startDate;
        document.getElementById("endDate").value = e.target.dataset.endDate;

        const techStack = e.target.dataset.techStack.split(',');
        techStackButtons.forEach(button => {
            if (techStack.includes(button.textContent)) {
                button.classList.add("selected");
            } else {
                button.classList.remove("selected");
            }
        });

        certificateGroup.style.display = "none";
        modal.style.display = "block";
    }

    if (e.target && e.target.classList.contains("delete-button")) {
        const profileId = e.target.dataset.profileId;

        Swal.fire({
            title: '정말 삭제하겠습니까?',
            text: "이 작업은 되돌릴 수 없습니다!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '예',
            cancelButtonText: '아니오'
        }).then((result) => {
            if (result.isConfirmed) {
                fetch(`/api/profiles/${profileId}`, {
                    method: "DELETE",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + accessToken
                    }
                })
                    .then(response => response.text())
                    .then(data => {
                        console.log("삭제 성공:", data);
                        removeCardFromDOM(profileId);
                        Swal.fire({
                            toast: true,
                            position: 'center',
                            icon: 'success',
                            title: '프로필이 삭제되었습니다.',
                            showConfirmButton: false,
                            timer: 3000,
                            timerProgressBar: true,
                            didOpen: (toast) => {
                                toast.addEventListener('mouseenter', Swal.stopTimer);
                                toast.addEventListener('mouseleave', Swal.resumeTimer);
                            },
                            customClass: {
                                title: 'black-text'
                            }
                        });
                    })
                    .catch((error) => {
                        console.error("에러:", error);
                    });
            }
        });
    }
});

function fetchProfiles() {
    fetch("/api/profiles", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + accessToken
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log("프로필 데이터:", data);
            data.forEach(profile => {
                addCardToDOM(profile);
            });
        })
        .catch((error) => {
            console.error("에러:", error);
        });
}

function addCardToDOM(profile) {
    const newCard = document.createElement('div');
    newCard.className = 'card';
    newCard.innerHTML = `
        <div class="card-header">
            <div class="card-title">${profile.bootcampName}</div>
            <div class="card-actions">
                <button class="edit-button" data-profile-id="${profile.id}" data-bootcamp="${profile.bootcampName}" data-track="${profile.track}" data-generation="${profile.generation}" data-start-date="${profile.startDate}" data-end-date="${profile.endDate}" data-tech-stack="${(profile.techStack || []).join(',')}">수정</button>
                <button class="delete-button" data-profile-id="${profile.id}">삭제</button>
            </div>
        </div>
        <div class="card-content">
            <div class="info-column">
                <table class="info-table">
                    <tr>
                        <td>트랙</td>
                        <td>${profile.track}</td>
                    </tr>
                    <tr>
                        <td>기수</td>
                        <td>${profile.generation}</td>
                    </tr>
                    <tr>
                        <td>참여 기간</td>
                        <td>${profile.startDate} - ${profile.endDate}</td>
                    </tr>
                </table>
            </div>
            <div class="tech-stack-column">
                <div class="column-title">기술 스택</div>
                <div class="skill-container">
                    ${(profile.techStack || []).map(skill => `<span class="skill">${skill}</span>`).join('')}
                </div>
            </div>
        </div>
    `;

    const contentContainer = document.querySelector('.content-container');
    contentContainer.appendChild(newCard);
}

function updateCardInDOM(profileId, profileData) {
    const card = document.querySelector(`.edit-button[data-profile-id="${profileId}"]`).closest('.card');
    card.querySelector('.card-title').textContent = profileData.bootcampName;
    card.querySelector('.info-table').innerHTML = `
        <tr>
            <td>트랙</td>
            <td>${profileData.track}</td>
        </tr>
        <tr>
            <td>기수</td>
            <td>${profileData.generation}</td>
        </tr>
        <tr>
            <td>참여 기간</td>
            <td>${profileData.startDate} - ${profileData.endDate}</td>
        </tr>
    `;
    card.querySelector('.skill-container').innerHTML = profileData.techStack.map(skill => `<span class="skill">${skill}</span>`).join('');
}

function removeCardFromDOM(profileId) {
    const card = document.querySelector(`.delete-button[data-profile-id="${profileId}"]`).closest('.card');
    card.remove();
}

activityCards.forEach(card => {
    card.addEventListener("click", function() {
        const activityType = this.dataset.activity;
        let activityData = [];

        switch(activityType) {
            case "liked-bootcamps":
                activityModalTitle.textContent = "좋아요한 부트캠프";
                activityData = ["코드스테이츠", "패스트캠퍼스", "멋쟁이사자처럼", "네이버 부스트캠프", "우아한테크코스"];
                break;
            case "liked-reviews":
                activityModalTitle.textContent = "좋아요한 리뷰";
                activityData = [
                    "코드스테이츠 프론트엔드 과정 리뷰",
                    "패스트캠퍼스 백엔드 과정 리뷰",
                    "멋쟁이사자처럼 AI 과정 리뷰",
                    "네이버 부스트캠프 웹 풀스택 과정 리뷰",
                    "우아한테크코스 안드로이드 과정 리뷰"
                ];
                break;
            case "liked-studies":
                activityModalTitle.textContent = "좋아요한 스터디";
                activityData = [
                    "알고리즘 스터디",
                    "React 심화 스터디",
                    "Spring Boot 실전 프로젝트 스터디"
                ];
                break;
            case "written-reviews":
                activityModalTitle.textContent = "작성한 리뷰";
                activityData = [
                    "코드스테이츠 프론트엔드 과정 후기",
                    "패스트캠퍼스 백엔드 과정 경험담",
                    "네이버 부스트캠프를 통해 배운 점"
                ];
                break;
            case "written-questions":
                activityModalTitle.textContent = "작성한 질문";
                activityData = [
                    "React Hooks 사용 시 주의할 점",
                    "Spring Security 설정 방법",
                    "Docker 컨테이너 네트워킹 문제",
                    "MongoDB 인덱싱 최적화 방법"
                ];
                break;
            case "written-answers":
                activityModalTitle.textContent = "작성한 답변";
                activityData = [
                    "JavaScript 비동기 처리 방법 설명",
                    "REST API 설계 원칙 안내",
                    "Git 브랜치 전략 추천",
                    "CSS Flexbox vs Grid 사용 시기"
                ];
                break;
            case "written-studies":
                activityModalTitle.textContent = "작성한 스터디";
                activityData = [
                    "JavaScript 딥다이브 스터디",
                    "알고리즘 문제 풀이 스터디"
                ];
                break;
        }

        activityList.innerHTML = "";
        activityData.forEach(item => {
            const li = document.createElement("li");
            li.textContent = item;
            activityList.appendChild(li);
        });

        activityModal.style.display = "block";
    });
});
