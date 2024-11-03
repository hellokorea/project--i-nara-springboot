document.addEventListener('DOMContentLoaded', async () => {
    const currentUrl = window.location.href;
    const url = new URL(currentUrl);
    const isbn = url.searchParams.get('bookIsbn');
    const token = localStorage.getItem('Authorization');
    try {
        // 책 상세 정보 요청
        const response = await fetch(`/members/books/detail/${isbn}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        });
        if (!response.ok) {
            throw new Error('책 정보를 가져오는데 실패했습니다.');
        }

        const data = await response.json();
        const bookDetail = data.data;
        document.querySelector('.book-image').src = "../images/coverImage.jpg";
        document.getElementById('book-title').innerText = bookDetail.title;
        document.getElementById('book-author').innerText = bookDetail.author;
        document.getElementById('book-plot').innerText = bookDetail.plot;

        const keywordsDiv = document.getElementById('book-keywords');
        const keywordsArray = bookDetail.keyword.split(', ');
        keywordsArray.forEach(keyword => {
            const keywordTag = document.createElement('div');
            keywordTag.classList.add('keyword-tag');
            keywordTag.innerText = keyword;
            keywordsDiv.appendChild(keywordTag);
        });

        const recommendedAgeTag = document.createElement('div');
        recommendedAgeTag.classList.add('keyword-tag');
        recommendedAgeTag.innerText = bookDetail.recommendedAge;
        keywordsDiv.appendChild(recommendedAgeTag);

        const categoryTag = document.createElement('div');
        categoryTag.classList.add('keyword-tag');
        categoryTag.innerText = bookDetail.category;
        keywordsDiv.appendChild(categoryTag);

        const similarBooksResponse = await fetch(`/members/books/similar?isbn=${isbn}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        });

        if (!similarBooksResponse.ok) {
            throw new Error('유사한 책 정보를 가져오는데 실패했습니다.');
        }

        const similarBooksData = await similarBooksResponse.json();
        const similarBooks = similarBooksData.data;

        const similarBooksContainer = document.getElementById('similar-books');
        similarBooksContainer.innerHTML = '';

        similarBooks.forEach(book => {
            const bookDiv = document.createElement('div');
            bookDiv.classList.add('similar-book-item');

            const bookImage = document.createElement('img');
            bookImage.src = "../images/coverImage.jpg";
            bookImage.alt = book.title;
            bookImage.classList.add('similar-book-image');

            bookImage.addEventListener('click', () => {
                location.href = `/html/bookdetail.html?bookIsbn=`+book.isbn;
            });

            const bookTitle = document.createElement('p');
            bookTitle.innerText = book.title;

            bookDiv.appendChild(bookImage);
            bookDiv.appendChild(bookTitle);
            similarBooksContainer.appendChild(bookDiv);
        });
    } catch (error) {
        console.error(error);
        alert('책 정보를 불러오는 중 오류가 발생했습니다.');
    }
});

document.getElementById('read-button').addEventListener('click', async () => {
    const currentUrl = window.location.href;
    const url = new URL(currentUrl);
    const isbn = url.searchParams.get('bookIsbn');
    const token = localStorage.getItem('Authorization');
    const childId = localStorage.getItem('childId');
    try {
        const response = await fetch(`/members/books/read/${isbn}?childId=${childId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        });
        if (!response.ok) {
            throw new Error('책 읽기 요청에 실패했습니다.');
        }

        // const data = await response.json();
        alert("도서를 읽었습니다.");
    } catch (error) {
        console.error(error);
        alert('책 읽기 중 오류가 발생했습니다.');
    }
});

document.getElementById('like-button').addEventListener('click', async () => {
    const currentUrl = window.location.href;
    const url = new URL(currentUrl);
    const isbn = url.searchParams.get('bookIsbn');
    const token = localStorage.getItem('Authorization');
    const childId = localStorage.getItem('childId');

    const request = { childId: childId, type: '0300_02' };
    try {
        const response = await fetch(`/books/${isbn}/likes`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            },
            body: JSON.stringify(request)
        });

        if (!response.ok) {
            throw new Error('좋아요 등록에 실패했습니다.');
        }

        const data = await response.json();
        alert(data.message);
    } catch (error) {
        console.error(error);
        alert('좋아요 등록 중 오류가 발생했습니다.');
    }
});