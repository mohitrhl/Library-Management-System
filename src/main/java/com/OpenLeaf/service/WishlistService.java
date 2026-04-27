package com.OpenLeaf.service;

import com.OpenLeaf.exception.BookException;
import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.exception.WishlistException;
import com.OpenLeaf.payload.dto.WishlistDTO;
import com.OpenLeaf.payload.response.PageResponse;

public interface WishlistService {

    WishlistDTO addToWishlist(Long bookId, String notes) throws BookException, WishlistException, UserException;

    void removeFromWishlist(Long bookId) throws WishlistException, UserException;

    PageResponse<WishlistDTO> getMyWishlist(int page, int size) throws UserException;

    PageResponse<WishlistDTO> getUserWishlist(Long userId, int page, int size);

    boolean isBookInWishlist(Long bookId) throws UserException;

    WishlistDTO updateWishlistNotes(Long bookId, String notes) throws WishlistException, UserException;

    Long getMyWishlistCount() throws UserException;

    Long getBookWishlistCount(Long bookId);
}
