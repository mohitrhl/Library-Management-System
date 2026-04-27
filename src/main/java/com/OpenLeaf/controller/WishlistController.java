package com.OpenLeaf.controller;

import com.OpenLeaf.exception.BookException;
import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.exception.WishlistException;
import com.OpenLeaf.payload.dto.WishlistDTO;
import com.OpenLeaf.payload.response.ApiResponse;
import com.OpenLeaf.payload.response.PageResponse;
import com.OpenLeaf.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // ==================== WISHLIST CRUD OPERATIONS ====================


    @PostMapping("/add/{bookId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> addToWishlist(
            @PathVariable Long bookId,
            @RequestParam(required = false) String notes) {
        try {
            WishlistDTO wishlist = wishlistService.addToWishlist(bookId, notes);
            return new ResponseEntity<>(wishlist, HttpStatus.CREATED);
        } catch (BookException | WishlistException | UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/remove/{bookId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse> removeFromWishlist(@PathVariable Long bookId) {
        try {
            wishlistService.removeFromWishlist(bookId);
            return ResponseEntity.ok(new ApiResponse("Book removed from wishlist successfully", true));
        } catch (WishlistException | UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("/update-notes/{bookId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> updateWishlistNotes(
            @PathVariable Long bookId,
            @RequestParam(required = false) String notes) {
        try {
            WishlistDTO wishlist = wishlistService.updateWishlistNotes(bookId, notes);
            return ResponseEntity.ok(wishlist);
        } catch (WishlistException | UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // ==================== GET WISHLIST ====================


    @GetMapping("/my-wishlist")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageResponse<WishlistDTO>> getMyWishlist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws UserException {

        PageResponse<WishlistDTO> wishlist = wishlistService.getMyWishlist(page, size);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponse<WishlistDTO>> getUserWishlist(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<WishlistDTO> wishlist = wishlistService.getUserWishlist(userId, page, size);
        return ResponseEntity.ok(wishlist);
    }

    // ==================== WISHLIST CHECKS ====================

    @GetMapping("/check/{bookId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<IsInWishlistResponse> checkIfInWishlist(@PathVariable Long bookId) throws UserException {
        boolean isInWishlist = wishlistService.isBookInWishlist(bookId);
        return ResponseEntity.ok(new IsInWishlistResponse(isInWishlist));
    }


    @GetMapping("/my-count")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CountResponse> getMyWishlistCount() throws UserException {
        Long count = wishlistService.getMyWishlistCount();
        return ResponseEntity.ok(new CountResponse(count));
    }


    @GetMapping("/book/{bookId}/count")
    public ResponseEntity<CountResponse> getBookWishlistCount(@PathVariable Long bookId) {
        Long count = wishlistService.getBookWishlistCount(bookId);
        return ResponseEntity.ok(new CountResponse(count));
    }

    // ==================== RESPONSE DTOs ====================


    public static class IsInWishlistResponse {
        public boolean isInWishlist;

        public IsInWishlistResponse(boolean isInWishlist) {
            this.isInWishlist = isInWishlist;
        }
    }

    public static class CountResponse {
        public Long count;

        public CountResponse(Long count) {
            this.count = count;
        }
    }
}
