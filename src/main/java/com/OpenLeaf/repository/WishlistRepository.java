package com.OpenLeaf.repository;

import com.OpenLeaf.modal.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {


    Page<Wishlist> findByUserId(Long userId, Pageable pageable);

    Optional<Wishlist> findByUserIdAndBookId(Long userId, Long bookId);

    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    void deleteByUserIdAndBookId(Long userId, Long bookId);

    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.book.id = :bookId")
    Long countByBookId(@Param("bookId") Long bookId);
}
