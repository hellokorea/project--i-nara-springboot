
document.addEventListener('DOMContentLoaded', () => {
    const queryParams = new URLSearchParams(window.location.search);
    const valueData = {
        I: parseInt(queryParams.get('I')) || 0,
        E: parseInt(queryParams.get('E')) || 0,
        S: parseInt(queryParams.get('S')) || 0,
        N: parseInt(queryParams.get('N')) || 0,
        T: parseInt(queryParams.get('T')) || 0,
        F: parseInt(queryParams.get('F')) || 0,
        J: parseInt(queryParams.get('J')) || 0,
        P: parseInt(queryParams.get('P')) || 0
    };

    updateTraitBars(valueData);
});

function updateTraitBars(valueData) {
    const traitPairs = [
        { left: 'I', right: 'E', leftColor: '#87ce85', rightColor: '#ffb6b9' },
        { left: 'S', right: 'N', leftColor: '#fdc84c', rightColor: '#81e6d9' },
        { left: 'T', right: 'F', leftColor: '#9d72e8', rightColor: '#ff8787' },
        { left: 'J', right: 'P', leftColor: '#6ee7b7', rightColor: '#f9a8d4' }
    ];

    traitPairs.forEach(({ left, right, leftColor, rightColor }) => {
        const leftValue = valueData[left];
        const rightValue = valueData[right];
        const leftFill = document.getElementById(`${left}-fill`);
        const rightFill = document.getElementById(`${right}-fill`);
        const leftPercent = document.getElementById(`${left}-percent`);
        const rightPercent = document.getElementById(`${right}-percent`);

        // Update fill color and width based on the higher value
        if (leftValue > rightValue) {
            leftFill.style.width = `${leftValue}%`;
            leftFill.style.backgroundColor = leftColor;
            rightFill.style.width = `${rightValue}%`;
            rightFill.style.backgroundColor = '#ddd'; // 회색
        } else {
            leftFill.style.width = `${leftValue}%`;
            leftFill.style.backgroundColor = '#ddd'; // 회색
            rightFill.style.width = `${rightValue}%`;
            rightFill.style.backgroundColor = rightColor;
        }

        // Update percentage text
        leftPercent.textContent = `${leftValue}%`;
        rightPercent.textContent = `${rightValue}%`;

        // Hide the opposite percentage text if one side is 100%
        if (leftValue === 100) {
            rightPercent.style.opacity = '0';
            rightPercent.style.visibility = 'hidden';
            rightFill.style.width = '0'; // 반대쪽 바의 너비를 0으로 설정
        } else if (rightValue === 100) {
            leftPercent.style.opacity = '0';
            leftPercent.style.visibility = 'hidden';
            leftFill.style.width = '0'; // 반대쪽 바의 너비를 0으로 설정
        } else {
            leftPercent.style.opacity = '1';
            leftPercent.style.visibility = 'visible';
            rightPercent.style.opacity = '1';
            rightPercent.style.visibility = 'visible';
        }
    });
}
