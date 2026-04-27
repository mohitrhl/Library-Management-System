package com.OpenLeaf.controller;

import com.OpenLeaf.exception.BookException;
import com.OpenLeaf.exception.BookReviewException;
import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.payload.dto.BookRatingStatisticsDTO;
import com.OpenLeaf.payload.dto.BookReviewDTO;
import com.OpenLeaf.payload.request.CreateReviewRequest;
import com.OpenLeaf.payload.request.UpdateReviewRequest;
import com.OpenLeaf.payload.response.ApiResponse;
import com.OpenLeaf.payload.response.PageResponse;
import com.OpenLeaf.service.BookReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/reviews")
public class BookReviewController {

    private final BookReviewService bookReviewService;

    public BookReviewController(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    // ==================== REVIEW CRUD OPERATIONS ====================


    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> createReview(@Valid @RequestBody CreateReviewRequest request) {
        try {
            BookReviewDTO createdReview = bookReviewService.createReview(request);
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } catch (BookReviewException | BookException | UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @PutMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequest request) {
        try {
            BookReviewDTO updatedReview = bookReviewService.updateReview(reviewId, request);
            return ResponseEntity.ok(updatedReview);
        } catch (BookReviewException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId) {
        try {
            bookReviewService.deleteReview(reviewId);
            return ResponseEntity.ok(new ApiResponse("Review deleted successfully", true));
        } catch (BookReviewException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable Long reviewId) {
        try {
            BookReviewDTO review = bookReviewService.getReviewById(reviewId);
            return ResponseEntity.ok(review);
        } catch (BookReviewException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // ==================== GET REVIEWS BY BOOK ====================


    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<BookReviewDTO>> getReviewsByBook(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "ALL") com.OpenLeaf.domain.ReviewFilterType filter,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<BookReviewDTO> reviews = bookReviewService.getReviewsByBookWithFilter(
                bookId, filter, rating, page, size);
        return ResponseEntity.ok(reviews);
    }

    // ==================== GET REVIEWS BY USER ====================

    @GetMapping("/my-reviews")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<BookReviewDTO>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<BookReviewDTO> reviews = bookReviewService.getMyReviews(page, size);
        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponse<BookReviewDTO>> getReviewsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<BookReviewDTO> reviews = bookReviewService.getReviewsByUser(userId, page, size);
        return ResponseEntity.ok(reviews);
    }

    // ==================== RATING STATISTICS ====================


    @GetMapping("/book/{bookId}/statistics")
    public ResponseEntity<?> getRatingStatistics(@PathVariable Long bookId) {
        try {
            BookRatingStatisticsDTO statistics = bookReviewService.getRatingStatistics(bookId);
            return ResponseEntity.ok(statistics);
        } catch (BookException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // ==================== HELPFUL ACTIONS ====================


    @PostMapping("/{reviewId}/helpful")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> markReviewAsHelpful(@PathVariable Long reviewId) {
        try {
            BookReviewDTO updatedReview = bookReviewService.markReviewAsHelpful(reviewId);
            return ResponseEntity.ok(updatedReview);
        } catch (BookReviewException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // ==================== ELIGIBILITY CHECK ====================


    @GetMapping("/can-review/{bookId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CanReviewResponse> canReviewBook(@PathVariable Long bookId) {
        boolean canReview = bookReviewService.canUserReviewBook(bookId);
        return ResponseEntity.ok(new CanReviewResponse(canReview));
    }

    // ==================== ADMIN STATISTICS ====================



    @GetMapping("/admin/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewStatisticsResponse> getReviewStatistics() {
        long totalReviews = bookReviewService.getTotalReviewCount();
        return ResponseEntity.ok(new ReviewStatisticsResponse(totalReviews));
    }

    // ==================== RESPONSE DTOs ====================


    public static class CanReviewResponse {
        public boolean canReview;

        public CanReviewResponse(boolean canReview) {
            this.canReview = canReview;
        }
    }

    public static class ReviewStatisticsResponse {
        public long totalReviews;

        public ReviewStatisticsResponse(long totalReviews) {

            this.totalReviews = totalReviews;
        }
    }
}
