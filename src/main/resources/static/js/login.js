// 로그인 폼 요소 가져오기
const loginForm = document.querySelector('#loginForm');

// submit 이벤트 리스너 등록
loginForm.addEventListener('submit', handleSubmit);

async function handleSubmit(event) {
    event.preventDefault(); // 기본 제출 동작 막기
    console.log('로그인 시도');

    // 폼 데이터 가져오기
    const email = document.querySelector('#email').value;
    const password = document.querySelector('#password').value;

    // 입력값 검증
    if (!email || !password) {
        alert('이메일과 비밀번호를 입력해주세요.');
        return;
    }

    // API 요청 데이터 구성
    const loginData = {
        email: email,
        password: password
    };

    try {
        // API 호출
        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        console.log('서버 응답:', response);

        if (response.ok) {
            // 응답 데이터 처리
            const data = await response.json();
            console.log('로그인 성공:', data);

            // 토큰 저장
            const token = response.headers.get('Authorization');
            if (token) {
                localStorage.setItem('jwt', token);
                console.log('토큰 저장됨');
            }

            // 성공 메시지 표시 후 메인 페이지로 이동
            alert('로그인에 성공했습니다!');
            window.location.href = '/main';
        } else {
            // 에러 응답 처리
            const errorData = await response.json();
            console.error('로그인 실패:', errorData);
            alert(errorData.message || '로그인에 실패했습니다.');
        }
    } catch (error) {
        console.error('로그인 요청 중 오류 발생:', error);
        alert('로그인 처리 중 오류가 발생했습니다.');
    }
}

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', () => {
    console.log('로그인 페이지 로드됨');

    // 기존 토큰이 있다면 제거 (새로운 로그인을 위해)
    localStorage.removeItem('jwt');
});

function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.getElementById('toggleIcon');

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.src = 'images/눈2.png';  // 비밀번호 표시 중
        toggleIcon.alt = '비밀번호 숨기기';
    } else {
        passwordInput.type = 'password';
        toggleIcon.src = 'images/눈1.png';  // 비밀번호 숨김 중
        toggleIcon.alt = '비밀번호 보기';
    }
}