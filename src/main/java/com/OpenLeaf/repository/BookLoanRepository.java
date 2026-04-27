package com.OpenLeaf.repository;

import com.OpenLeaf.domain.BookLoanStatus;
import com.OpenLeaf.modal.BookLoan;
import com.OpenLeaf.modal.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {


    Page<BookLoan> findByUserId(Long userId, Pageable pageable);

    Page<BookLoan> findByBookId(Long bookId, Pageable pageable);


    @Query("SELECT bl FROM BookLoan bl WHERE bl.user.id = :userId AND bl.book.id = :bookId " +
           "AND (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE')")
    Optional<BookLoan> findActiveBookLoanByUserAndBook(
        @Param("userId") Long userId,
        @Param("bookId") Long bookId
    );

    Boolean existsByUserIdAndBookIdAndStatus(Long userId,
                                               Long bookId,
                                               BookLoanStatus activeStatuses);

    Page<BookLoan> findByStatus(BookLoanStatus status, Pageable pageable);


    Page<BookLoan> findByStatusAndUser(BookLoanStatus status, User user, Pageable pageable);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.dueDate < :currentDate " +
           "AND (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE')")
    Page<BookLoan> findOverdueBookLoans(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.dueDate = :date " +
           "AND bl.status = 'CHECKED_OUT'")
    List<BookLoan> findBookLoansDueOnDate(@Param("date") LocalDate date);

    @Query(""" 
SELECT bl FROM BookLoan bl WHERE bl.dueDate = :dueDate AND bl.status = :status 
""")
    Page<BookLoan> findBookLoansByDueDateAndStatus(
        @Param("dueDate") LocalDate dueDate,
        @Param("status") BookLoanStatus status,
        Pageable pageable
    );


    @Query("SELECT COUNT(bl) FROM BookLoan bl WHERE bl.user.id = :userId " +
           "AND (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE')")
    long countActiveBookLoansByUser(@Param("userId") Long userId);


    @Query("SELECT COUNT(bl) FROM BookLoan bl WHERE bl.user.id = :userId " +
           "AND bl.status = 'OVERDUE'")
    long countOverdueBookLoansByUser(@Param("userId") Long userId);


    @Query("SELECT CASE WHEN COUNT(bl) > 0 THEN true ELSE false END FROM BookLoan bl " +
           "WHERE bl.user.id = :userId AND bl.book.id = :bookId " +
           "AND (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE')")
    boolean hasActiveCheckout(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.checkoutDate BETWEEN :startDate AND :endDate")
    Page<BookLoan> findBookLoansByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );

    @Query("SELECT COUNT(bl) FROM BookLoan bl WHERE bl.checkoutDate BETWEEN :startDate AND :endDate")
    long countCheckoutsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT bl.book.id, bl.book.title, COUNT(bl) as count FROM BookLoan bl " +
           "GROUP BY bl.book.id, bl.book.title ORDER BY count DESC")
    List<Object[]> getMostBorrowedBooks(Pageable pageable);


    List<BookLoan> findByStatus(BookLoanStatus status);


    List<BookLoan> findByStatusAndDueDateBetween(BookLoanStatus status, LocalDate startDate, LocalDate endDate);
}
