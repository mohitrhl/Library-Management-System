package com.OpenLeaf.repository;

import com.OpenLeaf.modal.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {


    Page<BookReview> findByBookIdAndIsActiveTrue(Long bookId, Pageable pageable);

    Page<BookReview> findByUserIdAndIsActiveTrue(Long userId, Pageable pageable);

    Optional<BookReview> findByUserIdAndBookId(Long userId, Long bookId);


    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    @Query("SELECT AVG(br.rating) FROM BookReview br WHERE br.book.id = :bookId AND br.isActive = true")
    Double getAverageRatingByBookId(@Param("bookId") Long bookId);


    @Query("SELECT COUNT(br) FROM BookReview br WHERE br.book.id = :bookId AND br.isActive = true")
    Long countReviewsByBookId(@Param("bookId") Long bookId);


    Page<BookReview> findByBookIdAndRatingAndIsActiveTrue(Long bookId, Integer rating, Pageable pageable);


    Page<BookReview> findByBookIdAndIsVerifiedReaderTrueAndIsActiveTrue(Long bookId, Pageable pageable);


    @Query("SELECT br FROM BookReview br WHERE br.book.id = :bookId AND br.isActive = true ORDER BY br.helpfulCount DESC")
    Page<BookReview> findTopHelpfulReviewsByBookId(@Param("bookId") Long bookId, Pageable pageable);


    @Query("SELECT br.rating, COUNT(br) FROM BookReview br WHERE br.book.id = :bookId AND br.isActive = true GROUP BY br.rating")
    java.util.List<Object[]> countReviewsByRatingForBook(@Param("bookId") Long bookId);

    long countByIsActiveTrue();
}
