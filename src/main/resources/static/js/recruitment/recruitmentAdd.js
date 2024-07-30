const courseForm = document.getElementById('courseForm');
const classTypeSelect = document.getElementById('classTypeSelect');
const costTypeSelect = document.getElementById('costTypeSelect');
const fieldTypeSelect = document.getElementById('fieldTypeSelect');
const courseDifficultySelect = document.getElementById('courseDifficulty');
const selectedCategories = document.getElementById('selectedCategories');
const institutionInput = document.getElementById('institution');
const institutionResults = document.getElementById('institutionResults');
const submitButton = document.getElementById('submitButton');
const cancelButton = document.getElementById('cancelButton');

let selectedCategoryList = {
    classType: '',
    costType: '',
    fieldType: '',
    difficulty: ''
};

// 운영기관 목록 (실제로는 서버에서 가져와야 함)
const institutions = [
    "코딩 아카데미",
    "테크 학원",
    "IT 교육센터",
    "프로그래밍 스쿨",
    "개발자 양성소",
    "디지털 캠퍼스",
    "소프트웨어 교육원",
    "웹 개발 아카데미",
    "데이터 사이언스 스쿨",
    "AI 교육센터"
];

// Flatpickr 초기화
flatpickr("#coursePeriod", {
    mode: "range",
    dateFormat: "Y-m-d",
    disable: [
        function(date) {
            // 주말 선택 불가
            return (date.getDay() === 0 || date.getDay() === 6);
        }
    ],
    locale: {
        firstDayOfWeek: 1 // 월요일부터 시작
    }
});

function updateSelectedCategories() {
    selectedCategories.innerHTML = '';
    for (const [key, value] of Object.entries(selectedCategoryList)) {
        if (value) {
            const span = document.createElement('span');
            span.className = 'selected-category';
            span.innerHTML = `${value} <span class="remove-category" data-category="${key}">✕</span>`;
            selectedCategories.appendChild(span);
        }
    }

    // 삭제 버튼에 이벤트 리스너 추가
    document.querySelectorAll('.remove-category').forEach(button => {
        button.addEventListener('click', function() {
            const categoryToRemove = this.getAttribute('data-category');
            selectedCategoryList[categoryToRemove] = '';
            updateSelectedCategories();
            // 해당 select 요소의 선택을 초기화
            if (categoryToRemove === 'difficulty') {
                courseDifficultySelect.value = '';
            } else {
                document.getElementById(`${categoryToRemove}Select`).value = '';
            }
            validateForm();
        });
    });
}

[classTypeSelect, costTypeSelect, fieldTypeSelect, courseDifficultySelect].forEach(select => {
    select.addEventListener('change', function() {
        const categoryType = this.id === 'courseDifficulty' ? 'difficulty' : this.id.replace('Select', '');
        selectedCategoryList[categoryType] = this.value;
        updateSelectedCategories();
        validateForm();
    });
});

institutionInput.addEventListener('input', function() {
    const searchTerm = this.value.toLowerCase();
    const filteredInstitutions = institutions.filter(inst =>
        inst.toLowerCase().includes(searchTerm)
    );

    institutionResults.innerHTML = '';
    filteredInstitutions.forEach(inst => {
        const div = document.createElement('div');
        div.className = 'institution-item';
        div.textContent = inst;
        div.addEventListener('click', function() {
            institutionInput.value = inst;
            institutionResults.innerHTML = '';
            validateForm();
        });
        institutionResults.appendChild(div);
    });
});

// 다른 곳을 클릭하면 결과 목록 숨기기
document.addEventListener('click', function(e) {
    if (!institutionInput.contains(e.target) && !institutionResults.contains(e.target)) {
        institutionResults.innerHTML = '';
    }
});

function validateForm() {
    let isValid = true;
    const fields = [
        { id: 'courseTitle', errorId: 'courseTitleError', message: '과정 제목을 입력해주세요.' },
        { id: 'institution', errorId: 'institutionError', message: '운영기관을 선택해주세요.' },
        { id: 'courseIntro', errorId: 'courseIntroError', message: '과정 소개를 입력해주세요.' },
        { id: 'courseContent', errorId: 'courseContentError', message: '교육 내용을 입력해주세요.' },
        { id: 'coursePeriod', errorId: 'coursePeriodError', message: '기간을 선택해주세요.' },
        { id: 'courseTime', errorId: 'courseTimeError', message: '수업 시간을 입력해주세요.' }
    ];

    fields.forEach(field => {
        const element = document.getElementById(field.id);
        const errorElement = document.getElementById(field.errorId);
        if (!element.value.trim()) {
            errorElement.textContent = field.message;
            isValid = false;
        } else {
            errorElement.textContent = '';
        }
    });

    // 카테고리 유효성 검사
    const categoryError = document.getElementById('categoryError');
    if (!selectedCategoryList.classType || !selectedCategoryList.costType || !selectedCategoryList.fieldType || !selectedCategoryList.difficulty) {
        categoryError.textContent = '모든 카테고리를 선택해주세요.';
        isValid = false;
    } else {
        categoryError.textContent = '';
    }

    submitButton.disabled = !isValid;
}

// 모든 입력 필드에 대해 이벤트 리스너 추가
courseForm.querySelectorAll('input, textarea, select').forEach(element => {
    element.addEventListener('input', validateForm);
});

// 폼 제출 이벤트
courseForm.addEventListener('submit', function(e) {
    e.preventDefault();

    if (confirm('과정을 등록하시겠습니까?')) {
        window.location.href = '/camp';
        // 서버 처리용 코드
        // // 폼 데이터 수집
        // const formData = new FormData(courseForm);
        //
        // // 선택된 카테고리 추가
        // for (const [key, value] of Object.entries(selectedCategoryList)) {
        //     formData.append(key, value);
        // }
        //
        // // 서버로 데이터 전송
        // fetch('/api/courses', {  // 실제 API 엔드포인트로 수정 필요
        //     method: 'POST',
        //     body: formData
        // })
        //     .then(response => {
        //         if (response.ok) {
        //             alert('과정이 성공적으로 등록되었습니다.');
        //             // 목록 페이지로 이동
        //             window.location.href = '/api/camp';  // 실제 목록 페이지 URL로 수정 필요
        //         } else {
        //             throw new Error('과정 등록에 실패했습니다.');
        //         }
        //     })
        //     .catch(error => {
        //         alert(error.message);
        //     });
    }
});

// 취소 버튼 이벤트
cancelButton.addEventListener('click', function(e) {
    if (confirm('작성을 취소하시겠습니까? 입력한 내용은 저장되지 않습니다.')) {
        // 폼 초기화
        courseForm.reset();

        // 선택된 카테고리 초기화
        selectedCategoryList = {
            classType: '',
            costType: '',
            fieldType: '',
            difficulty: ''
        };
        updateSelectedCategories();

        // 목록 페이지로 이동
        window.location.href = '/camp';  // 실제 목록 페이지 URL로 수정 필요
    }
});

// 초기 카테고리 설정 및 폼 유효성 검사
updateSelectedCategories();
validateForm();