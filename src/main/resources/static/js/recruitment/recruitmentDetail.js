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
            courseContainer.classList.remove('edit-mode');
            courseContainer.classList.add('view-mode');
            
            // 수정된 내용을 비편집 요소에 반영
            document.querySelectorAll('.editable').forEach(elem => {
                const nonEditableElem = elem.previousElementSibling;
                if (nonEditableElem && nonEditableElem.classList.contains('non-editable')) {
                    if (elem.tagName === 'TEXTAREA') {
                        nonEditableElem.innerHTML = elem.value.replace(/\n/g, '<br>');
                    } else if (elem.tagName === 'SELECT') {
                        nonEditableElem.textContent = `난이도: ${elem.value}`;
                    } else if (elem.classList.contains('date-range')) {
                        const dates = elem.value.split(' to ');
                        const startDate = new Date(dates[0]);
                        const endDate = new Date(dates[1]);
                        const weeks = Math.ceil((endDate - startDate) / (7 * 24 * 60 * 60 * 1000));
                        nonEditableElem.textContent = `기간: ${elem.value.replace(' to ', ' ~ ')} (${weeks}주)`;
                    } else {
                        nonEditableElem.textContent = elem.value;
                    }
                }
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
            
            alert('변경사항이 저장되었습니다.');
        });

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
                if (['온라인', '오프라인', '혼합'].includes(text)) {
                    selectedCategoryList.classType = text;
                } else if (['국비', '유료', '무료'].includes(text)) {
                    selectedCategoryList.costType = text;
                } else {
                    selectedCategoryList.fieldType = text;
                }
            });
            updateSelectedCategories();
        });

        deleteButton.addEventListener('click', function() {
            if (confirm('정말로 이 과정을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
                alert('과정이 삭제되었습니다.');
                // 실제로는 여기서 서버에 삭제 요청을 보내고, 성공 시 페이지를 리디렉션하거나 DOM에서 요소를 제거해야 합니다.
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