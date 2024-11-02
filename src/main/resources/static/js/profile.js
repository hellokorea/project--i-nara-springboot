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

            // + 버튼은 항상 표시되도록 합니다.
            createProfileButton.style.display = 'block';

            profiles.forEach(profile => {
                const profileButton = document.createElement('button');
                profileButton.classList.add('profile-button');
                profileButton.innerHTML = `
                    <img src="/images/userImage.png" alt="프로필 이미지">
                    <span>${profile.childName} 프로필</span>
                `;
                profileButton.addEventListener('click', () => {
                    localStorage.setItem('childId', profile.childId);
                    window.location.href = '/main';
                });
                profileList.appendChild(profileButton);
            });

        } catch (error) {
            console.error('Error loading profile:', error);
            // 에러 처리 로직 추가 (예: 사용자에게 알림 표시)
        }
    }
}