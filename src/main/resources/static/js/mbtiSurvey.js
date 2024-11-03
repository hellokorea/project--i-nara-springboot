let currentQuestionIndex = 0;
let questions = [];
let responses = [];

document.addEventListener('DOMContentLoaded', () => {
    fetchQuestions();
});

const token = localStorage.getItem('Authorization');
const childId = localStorage.getItem('childId');

function fetchQuestions() {

    fetch(`/traits/mbti/${childId}`, {
        method: 'GET',
        headers: {
            'Authorization': token, // Authorization 헤더에 토큰 추가
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.status === 409) {
                window.location.href = "../html/mbtiReject.html";
            }
            return response.json();
        })
        .then(data => {
            questions = data.data;
            displayQuestion();
        })
        .catch(error => console.error('Error fetching questions:', error));
}

function displayQuestion() {
    if (currentQuestionIndex < 0 || currentQuestionIndex >= questions.length) return;

    const questionData = questions[currentQuestionIndex];
    const questionTitle = document.getElementById('question-title');
    const choicesContainer = document.getElementById('choices-container');
    const progressBar = document.getElementById('progress-bar');
    const previousBtn = document.querySelector('.previous-btn');

    questionTitle.textContent = `Q${currentQuestionIndex + 1}. ${questionData.content}`;
    choicesContainer.innerHTML = '';

    questionData.choices.forEach(choice => {
        const choiceElement = document.createElement('div');
        choiceElement.className = 'choice';
        choiceElement.textContent = choice.content;

        // 선택된 답변에 대한 색상 유지
        const selectedResponse = responses[currentQuestionIndex];
        if (selectedResponse && selectedResponse.answerId === choice.answerId) {
            choiceElement.classList.add('selected');
        }

        // 클릭 이벤트에 클릭 효과 추가
        choiceElement.onclick = () => {
            handleChoiceSelection(questionData.questionId, choice.answerId);
            markSelectedChoice(choice.answerId);
        };

        choicesContainer.appendChild(choiceElement);
    });

    // Progress bar update
    const progressPercentage = ((currentQuestionIndex + 1) / questions.length) * 100;
    progressBar.style.width = `${progressPercentage}%`;

    if (currentQuestionIndex === 0) {
        previousBtn.style.display = 'none';
    } else {
        previousBtn.style.display = 'inline-block';
    }
}

function handleChoiceSelection(questionId, answerId) {
    responses[currentQuestionIndex] = { questionId, answerId };

    // 선택된 답변에 대해 색상 유지
    markSelectedChoice(answerId);

    // 약간의 딜레이 후 다음 질문으로 넘어가기
    setTimeout(() => {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            displayQuestion();
        } else {
            submitResponses();
        }
    }, 220); // 300ms 딜레이, 필요에 따라 시간 조정 가능
}

function goToPrevious() {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        displayQuestion();
    }
}

function markSelectedChoice(answerId) {
    // 모든 선택지의 선택 상태를 초기화
    const choices = document.querySelectorAll('.choice');
    choices.forEach(choice => choice.classList.remove('selected'));

    // 선택된 답변에 대해 색상 유지
    const selectedChoice = Array.from(choices).find(choice => choice.textContent === questions[currentQuestionIndex].choices.find(c => c.answerId === answerId).content);
    if (selectedChoice) {
        selectedChoice.classList.add('selected');
    }
}

function submitResponses() {
    fetch(`/traits/mbti/${childId}`, {
        method: 'POST',
        headers: {
            'Authorization': token,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(responses)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('MBTI 결과 생성에 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            if (data) {
                window.location.href = '../html/mbtiPostResult.html';
            }
        })
        .catch(error => console.error('Error submitting responses:', error));
}


