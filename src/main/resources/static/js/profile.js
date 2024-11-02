// profile.js
document.addEventListener('DOMContentLoaded', function() {
    loadProfile();

    // 프로필 생성 버튼에 이벤트 리스너 추가
    const createProfileButton = document.getElementById('createProfileButton');
    createProfileButton.addEventListener('click', () => {
        window.location.href = '/profile/create';
    });
});

async function loadProfile() {
    const token = localStorage.getItem('Authorization');

    if (token) {
        try {
            const response = await fetch(`http://localhost:8080/members/children`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                }
            });

            if (!response.ok) {
                throw new Error('자녀 정보를 가져오는데 실패했습니다.');
            }

            const data = await response.json();
            const profiles = data.data;

            const profileList = document.querySelector('#profile-list');
            const createProfileButton = document.getElementById('createProfileButton');

            if (profiles.length === 0) {
                // 프로필이 없으면 + 버튼 표시
                createProfileButton.style.display = 'block';
            } else {
                // 프로필이 있으면 + 버튼 숨김
                createProfileButton.style.display = 'none';

                profiles.forEach(profile => {
                    const profileButton = document.createElement('button');
                    profileButton.textContent = `${profile.name} 프로필`;
                    profileButton.classList.add('profile-button');
                    profileButton.addEventListener('click', () => {
                        localStorage.setItem('childId', profile.childId);
                        window.location.href = '/main';
                    });
                    profileList.appendChild(profileButton);
                });

            }
        } catch (error) {
            console.error('Error loading profile:', error);
            // 에러 처리 로직 추가 (예: 사용자에게 알림 표시)
        }
    }
}