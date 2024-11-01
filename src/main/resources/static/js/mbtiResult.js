
document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname.endsWith('mbtiPostResult.html')) {
        document.querySelector('.result-button').addEventListener('click', fetchResult);
    }
});

function fetchResult() {
    const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3amRlaGRkazEyM0BuYXZlci5jb20iLCJpYXQiOjE3MzA0NDU3MDgsImV4cCI6MTczMDUzMjEwOH0.oU7UOMk4A1y0NztNI9bdt76xJmG9I3muTe30LNkiqpo';
    const childId = 3; // test

    fetch(`/traits/mbti/result/${childId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.status === 404) {
                window.location.href = '../html/error.html';
                return;
            }
            return response.json();
        })
        .then(data => {
            const resultData = data.data;
            const traitValue = resultData.traitValue.toUpperCase();

            // MBTI 결과에 따라 해당 HTML 파일로 리디렉션
            const queryParams = new URLSearchParams(resultData.valueData).toString();
            window.location.href = `/html/mbtiResultPages/${traitValue.toLowerCase()}.html?${queryParams}`;
        })
        .catch(error => console.error('Error fetching MBTI result:', error));
}
