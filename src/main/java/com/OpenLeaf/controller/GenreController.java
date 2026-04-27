package com.OpenLeaf.controller;

import com.OpenLeaf.exception.GenreException;
import com.OpenLeaf.payload.dto.GenreDTO;
import com.OpenLeaf.payload.response.ApiResponse;
import com.OpenLeaf.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    // ==================== CRUD OPERATIONS ====================

    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@Valid @RequestBody GenreDTO genreDTO) {
        try {
            GenreDTO createdGenre = genreService.createGenre(genreDTO);
            return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
        } catch (GenreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping("/bulk")
    public ResponseEntity<?> createGenresBulk(@Valid @RequestBody List<GenreDTO> genreDTOs) {
        try {
            List<GenreDTO> createdGenres = genreService.createGenresBulk(genreDTOs);
            return new ResponseEntity<>(createdGenres, HttpStatus.CREATED);
        } catch (GenreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @GetMapping("/code/{code}")
    public ResponseEntity<GenreDTO> getGenreByCode(@PathVariable String code) throws GenreException {
        GenreDTO genre = genreService.getGenreByCode(code);
        return ResponseEntity.ok(genre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDTO> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody GenreDTO genreDTO) {
        try {
            GenreDTO updatedGenre = genreService.updateGenre(id, genreDTO);
            return ResponseEntity.ok(updatedGenre);
        } catch (GenreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteGenre(@PathVariable Long id) {
        try {
            genreService.deleteGenre(id);
            return ResponseEntity.ok(new ApiResponse("Genre deleted successfully", true));
        } catch (GenreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ApiResponse> hardDeleteGenre(@PathVariable Long id) {
        try {
            genreService.hardDeleteGenre(id);
            return ResponseEntity.ok(new ApiResponse("Genre permanently deleted", true));
        } catch (GenreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    // ==================== QUERY OPERATIONS ====================

    @GetMapping("/active")
    public ResponseEntity<List<GenreDTO>> getAllActiveGenres() {
        List<GenreDTO> genres = genreService.getAllActiveGenres();
        return ResponseEntity.ok(genres);
    }


    @GetMapping("/active/hierarchy")
    public ResponseEntity<List<GenreDTO>> getAllActiveGenresWithHierarchy() {
        List<GenreDTO> genres = genreService.getAllActiveGenresWithSubGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/top-level")
    public ResponseEntity<List<GenreDTO>> getTopLevelGenres() {
        List<GenreDTO> genres = genreService.getTopLevelGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{parentId}/sub-genres")
    public ResponseEntity<List<GenreDTO>> getSubGenres(@PathVariable Long parentId) {
        try {
            List<GenreDTO> subGenres = genreService.getSubGenresByParentId(parentId);
            return ResponseEntity.ok(subGenres);
        } catch (GenreException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<GenreDTO>> searchGenres(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<GenreDTO> genrePage = genreService.searchGenres(term, pageable);

        return ResponseEntity.ok(genrePage);
    }

    // ==================== STATISTICS ====================

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalActiveGenres() {
        long count = genreService.getTotalActiveGenres();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/book-count")
    public ResponseEntity<Long> getBookCountByGenre(
        @PathVariable Long id) {
        try {
            long count = genreService.getBookCountByGenre(id);
            return ResponseEntity.ok(count);
        } catch (GenreException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/in-use")
    public ResponseEntity<Boolean> isGenreInUse(@PathVariable Long id) {
        try {
            boolean inUse = genreService.isGenreInUse(id);
            return ResponseEntity.ok(inUse);
        } catch (GenreException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
