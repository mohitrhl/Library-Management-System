package com.OpenLeaf.controller;

import com.OpenLeaf.domain.BookLoanStatus;
import com.OpenLeaf.exception.BookException;
import com.OpenLeaf.exception.BookLoanException;
import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.payload.*;
import com.OpenLeaf.payload.dto.BookLoanDTO;
import com.OpenLeaf.payload.request.CheckinRequest;
import com.OpenLeaf.payload.request.CheckoutRequest;
import com.OpenLeaf.payload.request.RenewalRequest;
import com.OpenLeaf.payload.request.BookLoanSearchRequest;
import com.OpenLeaf.payload.response.ApiResponse;
import com.OpenLeaf.payload.response.PageResponse;
import com.OpenLeaf.service.BookLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/book-loans")
@RequiredArgsConstructor
@Slf4j
public class BookLoanController {

    private final BookLoanService bookLoanService;

    // ==================== CHECKOUT OPERATIONS ====================

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> checkoutBook(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        try {
            log.info("Checkout request received for book ID: {}", checkoutRequest.getBookId());
            BookLoanDTO bookLoan = bookLoanService.checkoutBook(checkoutRequest);
            return new ResponseEntity<>(bookLoan, HttpStatus.CREATED);
        } catch (BookLoanException | BookException | UserException e) {
            log.error("Checkout failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @PostMapping("/checkout/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> checkoutBookForUser(
            @PathVariable Long userId,
            @Valid @RequestBody CheckoutRequest checkoutRequest) {
        try {
            log.info("Admin checkout request for user ID: {}, book ID: {}", userId, checkoutRequest.getBookId());
            BookLoanDTO bookLoan = bookLoanService.checkoutBookForUser(userId, checkoutRequest);
            return new ResponseEntity<>(bookLoan, HttpStatus.CREATED);
        } catch (BookLoanException | BookException | UserException e) {
            log.error("Checkout failed for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // ==================== CHECKIN OPERATIONS ====================


    @PostMapping("/checkin")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> checkinBook(@Valid @RequestBody CheckinRequest checkinRequest) {
        try {
            log.info("Checkin request received for loan ID: {}", checkinRequest.getBookLoanId());
            BookLoanDTO bookLoan = bookLoanService.checkinBook(checkinRequest);
            return ResponseEntity.ok(bookLoan);
        } catch (BookLoanException e) {
            log.error("Checkin failed for loan ID: {}", checkinRequest.getBookLoanId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // ==================== RENEWAL OPERATIONS ====================

    @PostMapping("/renew")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> renewCheckout(@Valid @RequestBody RenewalRequest renewalRequest) {
        try {
            log.info("Renewal request received for loan ID: {}", renewalRequest.getBookLoanId());
            BookLoanDTO bookLoan = bookLoanService.renewCheckout(renewalRequest);
            return ResponseEntity.ok(bookLoan);
        } catch (BookLoanException e) {
            log.error("Renewal failed for loan ID: {}", renewalRequest.getBookLoanId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // ==================== QUERY OPERATIONS ====================


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBookLoanById(@PathVariable Long id) {
        try {
            BookLoanDTO bookLoan = bookLoanService.getBookLoanById(id);
            return ResponseEntity.ok(bookLoan);
        } catch (BookLoanException e) {
            log.error("Book loan not found: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyBookLoans(
            @RequestParam(required = false) BookLoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            PageResponse<BookLoanDTO> bookLoans = bookLoanService.getMyBookLoans(status, page, size);
            return ResponseEntity.ok(bookLoans);
        } catch (Exception e) {
            log.error("Failed to fetch book loans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to fetch book loans: " + e.getMessage(), false));
        }
    }


    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserBookLoans(
            @PathVariable Long userId,
            @RequestParam(required = false) BookLoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            PageResponse<BookLoanDTO> bookLoans = bookLoanService.getUserBookLoans(
                    userId, status, page, size);
            return ResponseEntity.ok(bookLoans);
        } catch (Exception e) {
            log.error("Failed to fetch book loans for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to fetch book loans: " + e.getMessage(), false));
        }
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBookLoans(
            @RequestBody BookLoanSearchRequest searchRequest) {
        try {
            PageResponse<BookLoanDTO> bookLoans = bookLoanService.getBookLoans(searchRequest);
            return ResponseEntity.ok(bookLoans);
        } catch (Exception e) {
            log.error("Failed to search book loans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to search loans: " + e.getMessage(), false));
        }
    }



    // ==================== ADMIN OPERATIONS ====================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBookLoan(
            @PathVariable Long id,
            @Valid @RequestBody com.OpenLeaf.payload.request.UpdateBookLoanRequest updateRequest) {
        try {
            log.info("Admin update request for book loan ID: {}", id);
            BookLoanDTO bookLoan = bookLoanService.updateBookLoan(id, updateRequest);
            return ResponseEntity.ok(bookLoan);
        } catch (BookLoanException e) {
            log.error("Failed to update book loan: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @PostMapping("/admin/update-overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOverdueBookLoans() {
        try {
            log.info("Admin triggered overdue update");
            int updateCount = bookLoanService.updateOverdueBookLoans();
            return ResponseEntity.ok(new UpdateOverdueResponse(updateCount));
        } catch (Exception e) {
            log.error("Failed to update overdue loans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to update overdue loans: " + e.getMessage(), false));
        }
    }


    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCheckoutStatistics() {
        try {
            CheckoutStatistics statistics = bookLoanService.getCheckoutStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("Failed to fetch checkout statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to fetch statistics: " + e.getMessage(), false));
        }
    }

    // ==================== RESPONSE DTOs ====================


    public static class UnpaidFinesResponse {
        public BigDecimal totalUnpaidFines;

        public UnpaidFinesResponse(BigDecimal totalUnpaidFines) {

            this.totalUnpaidFines = totalUnpaidFines;
        }
    }

    public static class UpdateOverdueResponse {
        public int bookLoansUpdated;
        public String message;

        public UpdateOverdueResponse(int bookLoansUpdated) {
            this.bookLoansUpdated = bookLoansUpdated;
            this.message = bookLoansUpdated + " book loan(s) marked as overdue";
        }
    }

}
