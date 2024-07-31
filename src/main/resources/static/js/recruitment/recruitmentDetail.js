const courseContainer = document.getElementById('courseContainer');
const editButton = document.getElementById('editButton');
const saveButton = document.getElementById('saveButton');
const cancelButton = document.getElementById('cancelButton');
const deleteButton = document.getElementById('deleteButton');
const likeButton = document.getElementById('likeButton');
const likeCount = document.getElementById('likeCount');
const classTypeSelect = document.getElementById('classTypeSelect');
const costTypeSelect = document.getElementById('costTypeSelect');
const fieldTypeSelect = document.getElementById('fieldTypeSelect');
const selectedCategories = document.getElementById('selectedCategories');

let originalContent = {};
let selectedCategoryList = {
classType: '온라인',
costType: '국비',
fieldType: '풀스택'
};



// Flatpickr 초기화
flatpickr(".date-range", {
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
        document.getElementById(`${categoryToRemove}Select`).value = '';
    });
});
}

[classTypeSelect, costTypeSelect, fieldTypeSelect].forEach(select => {
select.addEventListener('change', function() {
    const categoryType = this.id.replace('Select', '');
    selectedCategoryList[categoryType] = this.value;
    updateSelectedCategories();
});
});

editButton.addEventListener('click', function() {
courseContainer.classList.remove('view-mode');
courseContainer.classList.add('edit-mode');

// 현재 내용 저장
document.querySelectorAll('.editable').forEach(elem => {
    originalContent[elem.className] = elem.value;
});

// 현재 선택된 카테고리로 select 요소 업데이트
for (const [key, value] of Object.entries(selectedCategoryList)) {
    const selectElem = document.getElementById(`${key}Select`);
    if (selectElem) {
        selectElem.value = value;
    }
}

updateSelectedCategories();
});

saveButton.addEventListener('click', function() {
  const formData = new FormData(document.getElementById('recruitmentForm'));
  const recruitmentData = Object.fromEntries(formData);

  // 날짜 형식 변환
  const dateRange = recruitmentData.dateRange.split(' to ');
  recruitmentData.campStart = dateRange[0];
  recruitmentData.campEnd = dateRange[1];
  delete recruitmentData.dateRange;

  // API 호출
  fetch('/api/camps/' + recruitmentId, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(recruitmentData)
  })
  .then(response => response.json())
  .then(data => {
    if (data.statuscode === '200') {
      alert('변경사항이 저장되었습니다.');
      // 화면 업데이트 로직
      updateViewMode(data.data);
    } else {
      alert('저장에 실패했습니다: ' + data.msg);
    }
  })
  .catch(error => console.error('Error:', error));
});

function updateViewMode(data) {
  // 제목 업데이트
  document.querySelector('.course-title.non-editable').textContent = data.title;
  document.querySelector('.course-title.editable').value = data.title;

  // 캠프 이름 업데이트
  document.querySelector('.course-institution.non-editable').textContent = data.campName;
  document.querySelector('.course-institution.editable').value = data.campName;

  // 과정 소개 업데이트
  document.querySelector('.course-intro.non-editable').textContent = data.process;
  document.querySelector('.course-intro.editable').textContent = data.process;

  // 과정 내용 업데이트
  document.querySelector('.course-info .non-editable').textContent = data.content;
  document.querySelector('.course-info .editable').value = data.content;

  // 기간 업데이트
  document.querySelector('.course-sidebar .non-editable').textContent = `기간: ${data.campStart} ~ ${data.campEnd}`;
  document.querySelector('.course-sidebar .editable.date-range').value = `${data.campStart} to ${data.campEnd}`;

  // 수업 시간 업데이트
  document.querySelector('.non-editable.class-time').textContent = `수업 시간: ${data.classTime}`;
  document.querySelector('.editable.class-time').value = data.classTime;

  // 난이도 드롭다운 업데이트
  document.querySelector('.non-editable.level').textContent = `난이도: ${data.level}`;
  document.querySelector('.course-sidebar select').value = data.level;

  // 수업 방식 드롭다운 업데이트
  document.querySelector('#classTypeSelect').value = data.place;

  // 비용 드롭다운 업데이트
  document.querySelector('#costTypeSelect').value = data.cost;

  // 학습 분야 드롭다운 업데이트
  document.querySelector('#fieldTypeSelect').value = data.trek;

  // 태그 업데이트
  let tags = data.tags || [];
  document.querySelectorAll('.course-tags .tag').forEach(tagElement => {
    tagElement.style.display = tags.includes(tagElement.textContent) ? 'inline' : 'none';
  });

  // 선택된 카테고리 업데이트
  updateSelectedCategories(data.selectedCategoryList || {});
}

let recruitmentId = '';

// 페이지 로드 시 데이터 가져오기
document.addEventListener('DOMContentLoaded', function() {
  const urlParts = window.location.pathname.split('/');
  recruitmentId = urlParts[urlParts.length - 1];

  fetch('/api/camps/' + recruitmentId)
  .then(response => response.json())
  .then(data => {
    if (data.statuscode === '200') {
      updateViewMode(data.data);
    } else {
      alert('데이터를 불러오는데 실패했습니다: ' + data.msg);
    }
  })
  .catch(error => console.error('Error:', error));
});

// 카테고리 업데이트
const tagContainer = document.querySelector('.course-tags');
tagContainer.innerHTML = '';
for (const value of Object.values(selectedCategoryList)) {
    if (value) {
        const tag = document.createElement('span');
        tag.className = 'tag';
        tag.textContent = value;
        tagContainer.appendChild(tag);
    }
}

cancelButton.addEventListener('click', function() {
courseContainer.classList.remove('edit-mode');
courseContainer.classList.add('view-mode');

// 원래 내용으로 복구
document.querySelectorAll('.editable').forEach(elem => {
    elem.value = originalContent[elem.className];
});

// 카테고리 복구
const tags = document.querySelectorAll('.course-tags .tag');
selectedCategoryList = {
    classType: '',
    costType: '',
    fieldType: ''
};
tags.forEach(tag => {
    const text = tag.textContent;
    if (['온라인', '오프라인'].includes(text)) {
        selectedCategoryList.classType = text;
    } else if (['국비', '유료', '무료'].includes(text)) {
        selectedCategoryList.costType = text;
    } else {
        selectedCategoryList.fieldType = text;
    }
});
updateSelectedCategories();
});

// 삭제 버튼
deleteButton.addEventListener('click', function() {
  if (confirm('정말로 이 리뷰를 삭제하시겠습니까?')) {
    fetch(`/api/camps/${recruitmentId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // Access Token 추가
      }
    })
    .then(response => response.json())
    .then(data => {
      if (data.statuscode === "204") { // 성공적으로 삭제됨
        Swal.fire({
          title: '삭제 완료',
          text: '모집글이 성공적으로 삭제되었습니다.',
          icon: 'success',
          confirmButtonText: '확인'
        }).then(() => {
          window.location.href = '/camp';
        });
      } else {
        console.error('에러:', data.msg);
        Swal.fire({
          title: '삭제 실패',
          text: '모집글 삭제 중 오류가 발생했습니다.',
          icon: 'error',
          confirmButtonText: '확인'
        });
      }
    })
    .catch(error => {
      console.error('API 호출 중 에러 발생:', error);
      Swal.fire({
        title: '삭제 실패',
        text: '네트워크 오류가 발생했습니다.',
        icon: 'error',
        confirmButtonText: '확인'
      });
    });
  }
});

// 좋아요 버튼 기능
let isLiked = false;
let currentLikes = 1234;

likeButton.addEventListener('click', function() {
  isLiked = !isLiked;
  currentLikes += isLiked ? 1 : -1;
  likeButton.classList.toggle('active', isLiked);
  likeCount.textContent = currentLikes.toLocaleString();
});

// 관심 목록 추가 버튼 클릭 이벤트
document.querySelector('.wishlist-button').addEventListener('click', function() {
  alert('관심 목록에 추가되었습니다!');
});

// 초기 카테고리 설정
updateSelectedCategories();