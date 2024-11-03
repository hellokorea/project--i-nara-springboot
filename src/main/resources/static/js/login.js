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

// 로그인 폼 제출 이벤트
document.getElementById('loginForm').addEventListener('submit', async function (e) {
    e.preventDefault(); // 기본 폼 제출 방지

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
            const role = response.headers.get('Role');
            console.log("getElementById: " + token);
            if (token) {
                localStorage.setItem('Authorization', `${token}`);
                redirectToAppropriatePage(role, token); // 역할 확인 후 적절한 페이지로 리다이렉트
            } else {
                console.log('토큰이 없습니다');
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

// 역할에 따라 적절한 페이지로 리다이렉트
function redirectToAppropriatePage(role, token) {
    // Admin 역할인 경우 바로 adminmain.html로 이동, 일반 사용자는 자녀 프로필 확인
    console.log("redirectToAppropriatePage : " + token);
    if (role === 'Admin') {
        loadAdminMain(token)
    } else {
        checkChildProfile(); // 일반 사용자는 자녀 프로필 확인
    }
}


async function loadAdminMain(token) {
    console.log(token);
    if (!token) {
        alert("로그인이 필요합니다.");
        return;
    }

    try {
        const response = await fetch("/admin", {
            method: "GET",
            headers: {
                "Authorization": `${token}`,
                "Role" : "Admin",
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            // 전체 페이지 리로딩
            window.location.href = "/admin";
            console.log("afterlogin : " + token);
        } else {
            alert("페이지를 불러오는 데 실패했습니다.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("오류가 발생했습니다. 다시 시도해주세요.");
    }
}



// 자녀 프로필 확인
async function checkChildProfile() {
    try {
        const token = localStorage.getItem('Authorization');
        const response = await fetch('http://localhost:8080/members/children', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        });

        if (response.ok) {
            const data = await response.json();
            const profiles = data.data;

            if (profiles.length === 0) {
                window.location.href = '/profile'; // 자녀 프로필이 없으면 프로필 생성 페이지로 이동
            } else {
                const childId = profiles[0].childId;
                localStorage.setItem('childId', childId);
                window.location.href = '/main'; // 자녀 프로필이 있으면 main 페이지로 이동
            }
        } else if (response.status === 403) {
            alert("접근 권한이 없습니다. 다시 로그인 해주세요."); // 403 오류 처리
        } else {
            const error = await response.json();
            alert(error.message || '자녀 정보를 가져오는데 실패했습니다.');
        }
    } catch (error) {
        console.error('프로필 로딩 중 오류:', error);
        alert('프로필 로딩 중 오류가 발생했습니다.');
    }
}
