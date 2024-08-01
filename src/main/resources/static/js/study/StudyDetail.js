document.addEventListener('DOMContentLoaded', function () {
  const pathSegments = window.location.pathname.split('/');
  currentStudyId = pathSegments[pathSegments.length - 1]; // URL에서 스터디 ID 추출

  if (currentStudyId) {
    fetchStudyDetails(currentStudyId);
    fetchComments(currentStudyId); // 댓글을 가져오는 함수 호출
  } else {
    console.error('스터디 ID가 제공되지 않았습니다.');
  }

  addHeartButtonListeners();
});

// 스터디 상세 정보를 가져오는 함수
function fetchStudyDetails(id) {
  fetch(`/api/studies/${id}`)  // 백틱(`)을 사용하여 템플릿 리터럴로 수정
  .then(response => response.json())
  .then(data => {
    if (data.statuscode === "200") {
      updateStudyDetails(data.data);
    } else {
      console.error('에러:', data.msg);
    }
  })
  .catch(error => console.error('API 호출 중 에러 발생:', error));
}

// 스터디 상세 정보를 화면에 표시하는 함수
function updateStudyDetails(study) {
  // 제목 설정
  document.querySelector('#title').textContent = study.title || '';

  // 카테고리 설정
  document.querySelector('#category').textContent = study.category || '';

  // 설명 설정
  document.querySelector('#description').textContent = study.content || '';

  // 최대 인원 설정
  document.querySelector('#maxMembers').textContent = study.maxCount || '';

  // 예상 기간 설정
  document.querySelector('#duration').textContent = study.periodExpected || '';

  // 모임 주기 설정
  document.querySelector('#meetingFrequency').textContent = study.cycle || '';
}

// 선택된 카테고리를 가져오는 함수
function getCheckedCategory() {
  const categoryRadios = document.querySelectorAll('.category-radio');
  for (const radio of categoryRadios) {
    if (radio.checked) {
      return radio.value;
    }
  }
  return ''; // 기본값으로 빈 문자열 반환
}
