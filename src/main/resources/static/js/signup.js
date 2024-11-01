// 비밀번호 표시/숨기기 토글 기능
const passwordToggle = document.querySelector('.password-toggle');
const passwordInput = document.getElementById('password');
const toggleIcon = document.getElementById('toggleIcon');

function togglePassword() {
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.src = 'images/눈2.png'; // 닫힌 눈 이미지로 변경
    } else {
        passwordInput.type = 'password';
        toggleIcon.src = 'images/눈1.png'; // 열린 눈 이미지로 변경
    }
}

// 회원가입 폼 제출 처리
const registerForm = document.getElementById('registerForm');

registerForm.addEventListener('submit', (event) => {
    event.preventDefault(); // 기본 폼 제출 방지

    // 입력 값 가져오기
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const name = document.getElementById('name').value;

    // 회원가입 API 호출
    fetch('/members/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            password: password,
            name: name
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    throw new Error(error.message);
                });
            }
            return response.json();
        })
        .then(data => {
            // 회원가입 성공 시 처리
            console.log('회원가입 성공:', data);
            alert('회원가입이 성공적으로 완료되었습니다!');
            // 로그인 페이지로 리디렉션
            window.location.href = '/login';
        })
        .catch(error => {
            // 회원가입 실패 시 처리
            console.error('회원가입 실패:', error);
            alert(error.message);
        });
});