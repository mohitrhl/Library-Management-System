package com.OpenLeaf.service;

import com.OpenLeaf.exception.BookException;
import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.payload.dto.BookDTO;
import com.OpenLeaf.payload.request.BookSearchRequest;
import com.OpenLeaf.payload.response.PageResponse;

import java.util.List;


public interface BookService {

    // ==================== CRUD OPERATIONS ====================

    BookDTO createBook(BookDTO bookDTO) throws BookException;

    List<BookDTO> createBooksBulk(List<BookDTO> bookDTOs) throws BookException;

    BookDTO getBookById(Long bookId) throws BookException, UserException;

    BookDTO getBookByIsbn(String isbn) throws BookException;

    BookDTO updateBook(Long bookId, BookDTO bookDTO) throws BookException;

    void deleteBook(Long bookId) throws BookException;

    void hardDeleteBook(Long bookId) throws BookException;

    // ==================== UNIFIED SEARCH ====================

    PageResponse<BookDTO> searchBooksWithFilters(BookSearchRequest searchRequest);

    long getTotalActiveBooks();

    long getTotalAvailableBooks();
}
