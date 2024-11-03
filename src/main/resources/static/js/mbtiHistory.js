
const token = localStorage.getItem('Authorization');
const childId = localStorage.getItem('childId');

document.addEventListener('DOMContentLoaded', () => {
    fetch(`/traits/mbti/history/${childId}`, {
        method: 'GET',
        headers: {
            'Authorization': token,
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data && data.data) {
                const currentMBTI = data.data.currentTraitValue;
                const currentMBTICreatedAt = data.data.currentTraitValueCreatedAt;
                document.getElementById('current-mbti').textContent = currentMBTI;
                displayTraitRecords(data.data.traitRecords);
                displayActionHistoryAndChanges(data.data.actionHistory, data.data.changeTraitHistory, currentMBTI, currentMBTICreatedAt);
            }
        })
        .catch(error => {
            console.error('MBTI 히스토리를 가져오는 중 오류 발생:', error);
        });
});

function displayTraitRecords(records) {
    const traitElements = [
        { trait: 'I', opposite: 'E', traitColor: '#87ce85', oppositeColor: '#ffb6b9' },
        { trait: 'S', opposite: 'N', traitColor: '#fdc84c', oppositeColor: '#81e6d9' },
        { trait: 'T', opposite: 'F', traitColor: '#9d72e8', oppositeColor: '#ff8787' },
        { trait: 'J', opposite: 'P', traitColor: '#6ee7b7', oppositeColor: '#f9a8d4' }
    ];

    traitElements.forEach(({ trait, opposite, traitColor, oppositeColor }) => {
        const traitValue = records[trait] || 0;
        const oppositeValue = records[opposite] || 0;
        const traitFill = document.getElementById(`${trait}-fill`);
        const oppositeFill = document.getElementById(`${opposite}-fill`);
        const traitPercent = document.getElementById(`${trait}-percent`);
        const oppositePercent = document.getElementById(`${opposite}-percent`);

        if (traitValue > oppositeValue) {
            traitFill.style.width = `${traitValue}%`;
            traitFill.style.backgroundColor = traitColor;
            oppositeFill.style.width = `${oppositeValue}%`;
            oppositeFill.style.backgroundColor = '#ddd';
        } else {
            traitFill.style.width = `${traitValue}%`;
            traitFill.style.backgroundColor = '#ddd';
            oppositeFill.style.width = `${oppositeValue}%`;
            oppositeFill.style.backgroundColor = oppositeColor;
        }

        traitPercent.textContent = `${traitValue}%`;
        oppositePercent.textContent = `${oppositeValue}%`;

        if (traitValue === 100) {
            oppositePercent.style.display = 'none';
            oppositeFill.style.width = '0';
        } else if (oppositeValue === 100) {
            traitPercent.style.display = 'none';
            traitFill.style.width = '0';
        } else {
            traitPercent.style.display = 'inline';
            oppositePercent.style.display = 'inline';
        }
    });
}

function displayActionHistoryAndChanges(actionHistory, changeTraitHistory, currentMBTI, currentMBTICreatedAt) {
    const container = document.getElementById('action-history');
    container.innerHTML = '<h3>활동 기록</h3>';

    const groupedActions = activityGroupByDate(actionHistory);
    const groupedChanges = groupChangeHistory(changeTraitHistory, currentMBTI, currentMBTICreatedAt);

    const allDates = Array.from(new Set([...Object.keys(groupedActions), ...Object.keys(groupedChanges)]))
        .sort((a, b) => new Date(b) - new Date(a));

    let fragment = document.createDocumentFragment();
    let previousDate = null;

    allDates.forEach(date => {
        if (previousDate !== null && previousDate !== date) {
            fragment.appendChild(document.createElement('hr'));
        }

        const dateHeader = document.createElement('h4');
        dateHeader.textContent = date;
        fragment.appendChild(dateHeader);

        if (groupedChanges[date] && groupedChanges[date].length > 0) {
            groupedChanges[date].forEach(change => {
                if (change.beforeTraitValue && change.afterTraitValue) {
                    const changeItem = document.createElement('p');
                    changeItem.textContent = `MBTI 변경! ${change.beforeTraitValue} → ${change.afterTraitValue}`;
                    changeItem.style.color = 'rgb(255, 69, 0)';
                    fragment.appendChild(changeItem);
                }
            });
        }

        if (groupedActions[date] && groupedActions[date].length > 0) {
            groupedActions[date].forEach(action => {
                const actionItem = document.createElement('p');
                actionItem.innerHTML = `<strong>${action.bookName}</strong> 책 ${action.actionCodeName} 👍 / ${action.traitCodeName} +${action.point}`;
                fragment.appendChild(actionItem);
            });
        }

        previousDate = date;
    });

    container.appendChild(fragment);
}

function activityGroupByDate(actions) {
    return actions.reduce((acc, action) => {
        const date = parseDate(action.createdAt);
        if (date) {
            if (!acc[date]) {
                acc[date] = [];
            }
            acc[date].push(action);
        }
        return acc;
    }, {});
}

function groupChangeHistory(changeTraitHistory, currentMBTI, currentMBTICreatedAt) {
    const groupedChanges = {};
    let afterTraitValue = currentMBTI;
    const initialDate = parseDate(currentMBTICreatedAt);

    if (changeTraitHistory.length > 0 && initialDate) {
        if (!groupedChanges[initialDate]) {
            groupedChanges[initialDate] = [];
        }
        groupedChanges[initialDate].push({ beforeTraitValue: changeTraitHistory[0]?.beforeTraitValue || '', afterTraitValue });
    }

    for (let i = changeTraitHistory.length - 1; i >= 0; i--) {
        const change = changeTraitHistory[i];
        const date = parseDate(change.createdAt);

        if (date && change.beforeTraitValue && change.afterTraitValue) {
            if (!groupedChanges[date]) {
                groupedChanges[date] = [];
            }

            groupedChanges[date].push({ beforeTraitValue: change.beforeTraitValue, afterTraitValue });
            afterTraitValue = change.beforeTraitValue;
        }
    }

    return groupedChanges;
}

function parseDate(dateString) {
    console.log('parseDate called with dateString:', dateString);
    if (!dateString) {
        console.error('dateString is undefined or null:', dateString);
        return null;
    }
    const parsedDate = new Date(dateString);
    if (isNaN(parsedDate.getTime())) {
        console.error('유효하지 않은 날짜 형식:', dateString);
        return null;
    }
    const formattedDate = parsedDate.toLocaleDateString("ko-KR", { year: 'numeric', month: '2-digit', day: '2-digit' });
    return formattedDate;
}



