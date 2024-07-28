const modal = document.getElementById("bootcampModal");
const activityModal = document.getElementById("activityModal");
const registerBtn = document.getElementById("openModal");
const span = document.getElementsByClassName("close")[0];
const activitySpan = document.getElementsByClassName("close")[1];
const form = document.getElementById("bootcampForm");
const techStackButtons = document.querySelectorAll(".tech-stack-button");
const editButtons = document.querySelectorAll(".edit-button");
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
let currentEditingBootcamp = null;

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

editButtons.forEach(button => {
    button.addEventListener("click", function() {
        isEditing = true;
        currentEditingBootcamp = this.dataset.bootcamp;
        modalTitle.textContent = "부트캠프 정보 수정";
        submitButton.textContent = "수정";

        // 기존 데이터를 폼에 채우기
        bootcampNameSelect.value = this.dataset.bootcamp;
        bootcampNameSelect.disabled = true;
        trackSelect.value = this.dataset.track;
        trackSelect.disabled = true;
        document.getElementById("generation").value = this.dataset.generation;
        document.getElementById("startDate").value = this.dataset.startDate;
        document.getElementById("endDate").value = this.dataset.endDate;

        // 기술 스택 선택
        const techStack = this.dataset.techStack.split(',');
        techStackButtons.forEach(button => {
            if (techStack.includes(button.textContent)) {
                button.classList.add("selected");
            } else {
                button.classList.remove("selected");
            }
        });

        certificateGroup.style.display = "none";
        modal.style.display = "block";
    });
});

span.onclick = function() {
    modal.style.display = "none";
}

activitySpan.onclick = function() {
    activityModal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
    if (event.target == activityModal) {
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

    console.log("부트캠프 이름:", bootcampName);
    console.log("트랙:", track);
    console.log("기수:", generation);
    console.log("시작일:", startDate);
    console.log("종료일:", endDate);
    console.log("선택된 기술 스택:", selectedTechStack);

    if (!isEditing) {
        console.log("인증서 파일:", certificateFile);
    }

    if (isEditing) {
        console.log("수정 중인 부트캠프:", currentEditingBootcamp);
        // 여기에 수정 로직을 추가합니다.
    } else {
        // 여기에 새로운 부트캠프 등록 로직을 추가합니다.
    }

    modal.style.display = "none";
    form.reset();
    techStackButtons.forEach(button => button.classList.remove("selected"));
    certificatePreview.style.display = "none";
});

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