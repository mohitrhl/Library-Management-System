package com.OpenLeaf.repository;

import com.OpenLeaf.modal.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByCode(String code);

    boolean existsByCode(String code);

    List<Genre> findByActiveTrueOrderByDisplayOrderAsc();

    List<Genre> findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();

    List<Genre> findByParentGenreIdAndActiveTrueOrderByDisplayOrderAsc(Long parentGenreId);

    long countByActiveTrue();

    @Query("SELECT g FROM Genre g WHERE " +
           "LOWER(g.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(g.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Genre> searchGenres(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE b.genre.id = :genreId")
    boolean isGenreInUse(@Param("genreId") Long genreId);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.genre.id = :genreId")
    long countBooksByGenre(@Param("genreId") Long genreId);
}
