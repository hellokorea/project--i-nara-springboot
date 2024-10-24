package com.eureka.mindbloom.recommend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eureka.mindbloom.book.dto.BookRecommendResponse;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.recommend.domain.BookRecommend;
import com.eureka.mindbloom.recommend.domain.BookRecommendLike;
import com.eureka.mindbloom.recommend.repository.BookRecommendLikeRepository;
import com.eureka.mindbloom.recommend.repository.BookRecommendRepository;
import com.eureka.mindbloom.recommend.eums.RecommendLikeType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
	private final BookRecommendRepository bookRecommendRepository;
	private final BookRecommendLikeRepository bookRecommendLikeRepository;
	private final ChildRepository childRepository;

	@Override
	public List<BookRecommendResponse> getRecommendBooks(Long childId) {
		List<BookRecommendResponse> books = bookRecommendRepository.findByChildIdAndCurrentDate(childId);
		return books;
	}

	@Override
	public void likeRecommendBook(Long childId, Long bookRecommendId, RecommendLikeType likeType) {
		Child child = childRepository.findById(childId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 자녀가 존재하지 않습니다."));
		BookRecommend bookRecommend = bookRecommendRepository.findById(bookRecommendId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 추천 도서가 존재하지 않습니다."));
		if(likeType == RecommendLikeType.LIKE) {
			bookRecommendLikeRepository.save(new BookRecommendLike(bookRecommend, child));
			return;
		}
		bookRecommendLikeRepository.deleteById(bookRecommendId);
	}
}
