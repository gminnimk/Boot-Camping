function performSearch(event) {
    event.preventDefault();
    const searchType = document.getElementById('searchType').value;
    const searchQuery = document.getElementById('searchQuery').value;

    console.log(`검색 유형: ${searchType}, 검색어: ${searchQuery}`);
    alert(`검색 유형: ${searchType}\n검색어: ${searchQuery}`);
}

function toggleFilter(element) {
    if (element) {
        element.classList.toggle('active');
        updateSelectedFilters();
    } else {
        const filterContainer = document.getElementById('filterContainer');
        filterContainer.style.display = filterContainer.style.display === 'none' ? 'block' : 'none';
    }
}

function resetFilters() {
    const activeFilters = document.querySelectorAll('.filter-option.active');
    activeFilters.forEach(filter => filter.classList.remove('active'));
    updateSelectedFilters();
}

function applyFilters() {
    updateSelectedFilters();
    toggleFilter();
}

function updateSelectedFilters() {
    const selectedFiltersContainer = document.getElementById('selectedFilters');
    selectedFiltersContainer.innerHTML = '';

    const activeFilters = document.querySelectorAll('.filter-option.active');
    activeFilters.forEach(filter => {
        const filterElement = document.createElement('span');
        filterElement.className = 'selected-filter';
        filterElement.innerHTML = `
        ${filter.textContent}
        <span class="remove" onclick="removeFilter(this, '${filter.textContent}')">&times;</span>
      `;
        selectedFiltersContainer.appendChild(filterElement);
    });
}

function removeFilter(element, filterText) {
    const filterOption = Array.from(document.querySelectorAll('.filter-option')).find(option => option.textContent === filterText);
    if (filterOption) {
        filterOption.classList.remove('active');
    }
    element.parentElement.remove();
}

function toggleSection(header) {
    const section = header.closest('.filter-section');
    section.classList.toggle('collapsed');
}