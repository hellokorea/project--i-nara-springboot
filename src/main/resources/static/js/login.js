// 비밀번호 표시/숨김 토글
function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.getElementById('toggleIcon');

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.src = 'images/눈2.png';
        toggleIcon.alt = '비밀번호 숨기기';
    } else {
        passwordInput.type = 'password';
        toggleIcon.src = 'images/눈1.png';
        toggleIcon.alt = '비밀번호 보기';
    }
}

// login.js
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault(); // 폼 기본 제출 방지

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    if (!email || !password) {
        alert('이메일과 비밀번호를 입력해주세요.');
        return;
    }

    try {
        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const token = response.headers.get('Authorization');
            if (token) {
                localStorage.setItem('jwt', token);
                window.location.href = '/main';
            } else {
                throw new Error('토큰이 없습니다');
            }
        } else {
            const errorData = await response.json();
            alert(errorData.message || '로그인에 실패했습니다.');
        }
    } catch (error) {
        console.error('로그인 요청 중 오류:', error);
        alert('로그인 처리 중 오류가 발생했습니다.');
    }
});