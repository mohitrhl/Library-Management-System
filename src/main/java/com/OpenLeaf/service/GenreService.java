package com.OpenLeaf.service;

import com.OpenLeaf.exception.GenreException;
import com.OpenLeaf.payload.dto.GenreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface GenreService {

    // ==================== CRUD OPERATIONS ====================

    GenreDTO createGenre(GenreDTO genreDTO) throws GenreException;

    List<GenreDTO> createGenresBulk(List<GenreDTO> genreDTOs) throws GenreException;

    GenreDTO getGenreById(Long genreId) throws GenreException;

    GenreDTO getGenreByCode(String code) throws GenreException;

    GenreDTO updateGenre(Long genreId, GenreDTO genreDTO) throws GenreException;

    void deleteGenre(Long genreId) throws GenreException;

    void hardDeleteGenre(Long genreId) throws GenreException;


    // ==================== QUERY OPERATIONS ====================
    List<GenreDTO> getAllActiveGenres();

    List<GenreDTO> getAllActiveGenresWithSubGenres();

    List<GenreDTO> getTopLevelGenres();

    List<GenreDTO> getSubGenresByParentId(Long parentGenreId) throws GenreException;

    Page<GenreDTO> searchGenres(String searchTerm, Pageable pageable);

    long getTotalActiveGenres();

    long getBookCountByGenre(Long genreId) throws GenreException;

    boolean isGenreInUse(Long genreId) throws GenreException;
}
