function initializeCategoryButtons(categoryCode) {
    const selectedButton = document.querySelector(`.category-button[data-category="${categoryCode}"]`);
    if (selectedButton) {
        selectedButton.classList.add('active');
    }

    const bookContainer = document.getElementById('book-container');
    let page = 0;
    let isLast = false;
    let sortOption = 'VIEWCOUNT'; // 기본 정렬 옵션 설정

    // 카테고리 버튼 클릭 시 동작
    document.querySelectorAll('.category-button').forEach(button => {
        button.addEventListener('click', () => {
            document.querySelectorAll('.category-button').forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');

            categoryCode = button.getAttribute('data-category');
            page = 0;
            isLast = false;
            bookContainer.innerHTML = '';
            loadBooks(categoryCode, sortOption); // 카테고리 코드와 정렬 옵션 전달
        });
    });

    document.getElementById('sortSelect').addEventListener('change', (event) => {
        sortOption = event.target.value;
        page = 0;
        isLast = false;
        bookContainer.innerHTML = '';
        loadBooks(categoryCode, sortOption);
    });

    // 책 목록을 로드하는 함수
    async function loadBooks(categoryCode, sortOption) {
        const token = localStorage.getItem('Authorization');
        try {
            const response = await fetch(`http://localhost:8080/members/books?categoryCode=${categoryCode}&sortOption=${sortOption}&page=${page}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `${token}`
                }
            });
            if (!response.ok) {
                console.error('책 목록을 가져오는데 실패했습니다.');
                return;
            }

            const result = await response.json();
            const data = result.data.books;

            data.forEach(book => {
                const bookElement = document.createElement('div');
                bookElement.className = 'book';
                bookElement.innerHTML = `
                    <img src="../images/coverImage.jpg" alt="${book.title}" />
                    <p><strong>${book.title}</strong></p>
                `;
                bookElement.addEventListener('click', () => {
                    window.location.href = `/detail/${book.isbn}`;
                });
                bookContainer.appendChild(bookElement);
            });

            // 마지막 페이지 여부 갱신
            isLast = result.data.isLast;

            // 마지막 페이지가 아니라면, Observer를 통해 다음 로드를 준비
            if (!isLast) {
                const lastBook = document.querySelector('#book-container .book:last-child');
                observer.observe(lastBook);
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    }

    const observer = new IntersectionObserver(async (entries) => {
        if (entries[0].isIntersecting && !isLast) {
            page++;
            await loadBooks(categoryCode, sortOption);
        }
    }, {threshold: 1.0});

    // 초기 책 목록 로드
    loadBooks(categoryCode, sortOption);
}
function openSearch() {
    document.getElementById("myOverlay").style.display = "block";
}

function closeSearch() {
    document.getElementById("myOverlay").style.display = "none";
}