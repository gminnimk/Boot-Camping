// 검색 폼 제출 시 호출되는 함수 (검색 기능 함수)
function performSearch(event) {
  event.preventDefault();
  const searchType = document.getElementById('searchType').value;
  const searchQuery = document.getElementById('searchQuery').value;

  console.log(`검색 유형: ${searchType}, 검색어: ${searchQuery}`);
  alert(`검색 유형: ${searchType}\n검색어: ${searchQuery}`);
}

// 필터 버튼 클릭 시 필터 메뉴 토글
function toggleFilter(element) {
  if (element) {
    element.classList.toggle('active');
    updateSelectedFilters();
  } else {
    const filterContainer = document.getElementById('filterContainer');
    filterContainer.style.display = filterContainer.style.display === 'none'
        ? 'block' : 'none';
  }
}

// 필터 초기화
function resetFilters() {
  const activeFilters = document.querySelectorAll('.filter-option.active');
  activeFilters.forEach(filter => filter.classList.remove('active'));
  updateSelectedFilters();
}

// 필터링 요청을 서버에 전송하는 함수
// 필터 요청을 서버에 전송하여 필터링된 결과를 가져옵니다.
function applyFilters() {
  const filterRequest = getFilterRequest(); // 현재 필터 요청 객체를 가져옵니다.

  // 필터 요청 객체를 쿼리 문자열로 변환합니다.
  const queryString = Object.keys(filterRequest)
  .filter(key => filterRequest[key] !== null)
  .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(
      filterRequest[key])}`)
  .join('&');

  fetch(`/ranks/filter?${queryString}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => response.json()) // 응답을 JSON으로 파싱합니다.
  .then(data => {
    console.log('Filtered Results:', data); // 필터링된 결과를 콘솔에 출력합니다.
    localStorage.setItem('filteredResults', JSON.stringify(data)); // 필터링된 결과를 로컬 스토리지에 저장합니다.
    updateResults(data);
    // window.location.href = '/rank'; // 필터링된 결과를 표시할 페이지로 이동합니다.
  })
  .catch(error => console.error('Error:', error)); // 오류가 발생하면 콘솔에 출력합니다.

  toggleFilter(); // 필터 메뉴를 닫습니다.
}

// 활성화된 필터 옵션들을 확인하고, 이를 필터 요청 객체로 변환하는 함수
// 활성화된 필터 옵션을 기반으로 필터 요청 객체를 생성합니다.
function getFilterRequest() {
  const activeFilters = document.querySelectorAll('.filter-option.active');

  const filterRequest = {
    track: Array.from(activeFilters)
    .filter(el => ['프론트엔드', '백엔드', '풀스택', 'Android', 'iOS', '게임', 'AI', '데이터',
      'UI/UX'].includes(el.textContent.trim()))
    .map(el => el.textContent.trim())[0] || null, // 트랙 필터 요청을 설정합니다.
    environment: Array.from(activeFilters)
    .filter(el => ['온라인', '오프라인', '혼합'].includes(el.textContent.trim()))
    .map(el => el.textContent.trim())[0] || null, // 환경 필터 요청을 설정합니다.
    cost: Array.from(activeFilters)
    .filter(el => ['국비', '사비', '무료'].includes(el.textContent.trim()))
    .map(el => el.textContent.trim())[0] || null // 비용 필터 요청을 설정합니다.
  };

  return filterRequest;
}

// 필터 옵션 선택 및 업데이트
// 선택된 필터 옵션을 화면 상단의 선택된 필터 영역에 추가합니다.
function updateSelectedFilters() {
  const selectedFiltersContainer = document.getElementById('selectedFilters');
  selectedFiltersContainer.innerHTML = ''; // 기존의 선택된 필터를 지웁니다.

  const activeFilters = document.querySelectorAll('.filter-option.active');
  activeFilters.forEach(filter => {
    const filterElement = document.createElement('span');
    filterElement.className = 'selected-filter';
    filterElement.innerHTML = `
      ${filter.textContent}
      <span class="remove" onclick="removeFilter(this, '${filter.textContent}')">&times;</span>
    `; // 선택된 필터를 화면에 표시하고, 제거 버튼을 추가합니다.
    selectedFiltersContainer.appendChild(filterElement); // 선택된 필터를 선택된 필터 컨테이너에 추가합니다.
  });
}

// 선택된 필터를 비활성화하고 화면에서 제거하는 함수
function removeFilter(element, filterText) {
  const filterOption = Array.from(
      document.querySelectorAll('.filter-option')).find(
      option => option.textContent === filterText);
  if (filterOption) {
    filterOption.classList.remove('active'); // 필터 옵션에서 'active' 클래스를 제거합니다.
  }
  element.parentElement.remove(); // 선택된 필터 요소를 화면에서 제거합니다.
}

// 필터 섹션의 접기/펼치기 상태를 토글합니다.
function toggleSection(header) {
  const section = header.closest('.filter-section');
  section.classList.toggle('collapsed'); // 필터 섹션의 'collapsed' 클래스를 토글합니다.
}

// 필터 적용 버튼 클릭 시 필터를 적용하는 함수를 호출합니다.
document.querySelector('.main-filter-apply').addEventListener('click',
    applyFilters);