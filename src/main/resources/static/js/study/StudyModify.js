document.addEventListener('DOMContentLoaded', function () {
  const studyForm = document.getElementById('studyForm');
  const cancelButton = document.querySelector('.cancel-button');

  // 현재 스터디 ID를 URL에서 추출하는 함수
  function getCurrentStudyId() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('studyId');
  }

  // 스터디 데이터를 가져와 폼에 미리 채우는 함수
  function populateFormWithStudyData(studyId) {
    fetch(`/api/studies/${studyId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    })
    .then(response => response.json())
    .then(data => {
      if (data.statuscode === "200") {
        const study = data.data;
        document.getElementById('title').value = study.title || '';
        document.getElementById('category').value = study.category || '';
        document.getElementById('maxMembers').value = study.maxCount || ''; // maxCount로 변경
        document.getElementById('duration').value = study.periodExpected || ''; // periodExpected로 변경
        document.getElementById('frequency').value = study.cycle || ''; // cycle로 변경
        document.getElementById('description').value = study.content || ''; // content로 변경
      } else {
        console.error('에러:', data.msg);
        Swal.fire({
          title: '오류',
          text: '스터디 정보를 불러오는 중 오류가 발생했습니다.',
          icon: 'error',
          confirmButtonText: '확인'
        });
      }
    })
    .catch(error => {
      console.error('API 호출 중 에러 발생:', error);
      Swal.fire({
        title: '오류',
        text: '네트워크 오류가 발생했습니다.',
        icon: 'error',
        confirmButtonText: '확인'
      });
    });
  }

  // 폼 제출 시 호출되는 함수
  function handleSubmit(event) {
    event.preventDefault(); // 폼의 기본 제출 동작을 막습니다.

    const studyId = getCurrentStudyId();

    const updatedStudy = {
      title: document.getElementById('title').value,
      content: document.getElementById('description').value, // DTO에서는 content 필드 사용
      category: document.getElementById('category').value,
      maxCount: parseInt(document.getElementById('maxMembers').value, 10), // DTO에서는 maxCount 필드 사용
      periodExpected: document.getElementById('duration').value, // DTO에서는 periodExpected 필드 사용
      cycle: document.getElementById('frequency').value // DTO에서는 cycle 필드 사용
    };

    console.log('Submitting study data:', updatedStudy);

    fetch(`/api/studies/${studyId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      },
      body: JSON.stringify(updatedStudy)
    })
    .then(response => response.json())
    .then(data => {
      console.log('API response:', data);
      if (data.statuscode === "200") {
        Swal.fire({
          title: 'Update Successful',
          text: 'Study information has been successfully updated.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          window.location.href = `/study/detail/${studyId}`;
        });
      } else {
        console.error('Update error:', data.msg);
        Swal.fire({
          title: 'Update Failed',
          text: `Error updating study: ${data.msg || 'Unknown error'}`,
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    })
    .catch(error => {
      console.error('Network error during update:', error);
      Swal.fire({
        title: 'Update Failed',
        text: 'Network error occurred.',
        icon: 'error',
        confirmButtonText: 'OK'
      });
    });
  }

  // 취소 버튼 클릭 시 호출되는 함수
  function handleCancel() {
    const cancelUrl = cancelButton.getAttribute('data-cancel-url');
    if (confirm('수정을 취소하시겠습니까? 변경사항이 저장되지 않습니다.')) {
      window.location.href = cancelUrl; // 취소 URL로 리다이렉트
    }
  }

  // 페이지 로드 시 스터디 데이터 가져와 폼에 채우기
  const studyId = getCurrentStudyId();
  if (studyId) {
    populateFormWithStudyData(studyId);
  }

  // 이벤트 리스너 추가
  studyForm.addEventListener('submit', handleSubmit);
  cancelButton.addEventListener('click', handleCancel);
});