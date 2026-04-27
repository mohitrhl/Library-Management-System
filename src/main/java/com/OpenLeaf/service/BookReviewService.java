package com.OpenLeaf.service;

import com.OpenLeaf.exception.BookException;
import com.OpenLeaf.exception.BookReviewException;
import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.payload.dto.BookRatingStatisticsDTO;
import com.OpenLeaf.payload.dto.BookReviewDTO;
import com.OpenLeaf.payload.request.CreateReviewRequest;
import com.OpenLeaf.payload.request.UpdateReviewRequest;
import com.OpenLeaf.payload.response.PageResponse;

public interface BookReviewService {

    BookReviewDTO createReview(CreateReviewRequest request) throws BookReviewException, BookException, UserException;

    BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) throws BookReviewException;
    void deleteReview(Long reviewId) throws BookReviewException;


    BookReviewDTO getReviewById(Long reviewId) throws BookReviewException;

    PageResponse<BookReviewDTO> getReviewsByBookWithFilter(
            Long bookId,
            com.OpenLeaf.domain.ReviewFilterType filterType,
            Integer rating,
            int page, int size);


    PageResponse<BookReviewDTO> getMyReviews(int page, int size);

    PageResponse<BookReviewDTO> getReviewsByUser(Long userId, int page, int size);

    BookRatingStatisticsDTO getRatingStatistics(Long bookId) throws BookException;

    BookReviewDTO markReviewAsHelpful(Long reviewId) throws BookReviewException;

    boolean canUserReviewBook(Long bookId);

    boolean canUserReviewBook(Long userId, Long bookId);

    long getTotalReviewCount();
}
