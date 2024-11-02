document.addEventListener('DOMContentLoaded', function() {
    // 날짜 선택 초기화
    initializeDateSelectors();

    // 카테고리 선택 관리
    let selectedCategories = new Set();
    initializeCategorySelection(selectedCategories);

    // 폼 제출 처리
    initializeFormSubmission(selectedCategories);
});

function initializeDateSelectors() {
    const yearSelect = document.getElementById('birthYear');
    const monthSelect = document.getElementById('birthMonth');
    const daySelect = document.getElementById('birthDay');

    // 연도 옵션 생성 (현재 연도부터 10년 전까지)
    const currentYear = new Date().getFullYear();
    for (let year = currentYear; year >= currentYear - 10; year--) {
        const option = document.createElement('option');
        option.value = year;
        option.textContent = year + '년';
        yearSelect.appendChild(option);
    }

    // 월 옵션 생성
    for (let month = 1; month <= 12; month++) {
        const option = document.createElement('option');
        option.value = month.toString().padStart(2, '0');
        option.textContent = month + '월';
        monthSelect.appendChild(option);
    }

    // 연도나 월 선택 시 일 업데이트
    function updateDays() {
        const year = yearSelect.value;
        const month = monthSelect.value;

        daySelect.innerHTML = '<option value="">일</option>';

        if (year && month) {
            const daysInMonth = new Date(year, month, 0).getDate();
            for (let day = 1; day <= daysInMonth; day++) {
                const option = document.createElement('option');
                option.value = day.toString().padStart(2, '0');
                option.textContent = day + '일';
                daySelect.appendChild(option);
            }
        }
    }

    yearSelect.addEventListener('change', updateDays);
    monthSelect.addEventListener('change', updateDays);
}

function initializeCategorySelection(selectedCategories) {
    document.querySelectorAll('.category-item').forEach(item => {
        item.addEventListener('click', () => {
            const categoryCode = item.dataset.code;

            if (item.classList.contains('selected')) {
                selectedCategories.delete(categoryCode);
                item.classList.remove('selected');
            } else {
                if (selectedCategories.size < 3) {
                    selectedCategories.add(categoryCode);
                    item.classList.add('selected');
                } else {
                    alert('최대 3개까지만 선택할 수 있습니다.');
                }
            }
        });
    });
}

function initializeFormSubmission(selectedCategories) {
    const form = document.getElementById('profileForm');

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        // 입력값 검증
        const childName = document.getElementById('childName').value;
        const year = document.getElementById('birthYear').value;
        const month = document.getElementById('birthMonth').value;
        const day = document.getElementById('birthDay').value;
        const gender = document.querySelector('input[name="gender"]:checked')?.value;

        if (!childName || !year || !month || !day || !gender || selectedCategories.size === 0) {
            alert('모든 필수 항목을 입력해주세요!');
            return;
        }

        const birthDate = `${year}-${month}-${day}`;

        // 프로필 데이터 구성
        const profileData = {
            name: childName,
            birthDate: birthDate,
            gender: gender,
            categories: Array.from(selectedCategories)
        };

        try {
            const token = localStorage.getItem('Authorization');
            if (!token) {
                window.location.href = '/login';
                return;
            }

            const response = await fetch('/members/children', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
                body: JSON.stringify(profileData)
            });

            console.log(profileData)

            console.log('Response status:', response.status);
            console.log('Response headers:', [...response.headers.entries()]);

            if (response.ok) {
                const result = await response.json();
                console.log('Profile created:', result);
                alert('자녀 프로필이 생성되었습니다!');
                window.location.href = '/profile';
            } else {
                const error = await response.json();
                alert(error.message || '프로필 생성에 실패했습니다.');
            }
        } catch (error) {
            console.error('Profile creation error:', error);
            alert('프로필 생성 중 오류가 발생했습니다.');
        }
    });
}