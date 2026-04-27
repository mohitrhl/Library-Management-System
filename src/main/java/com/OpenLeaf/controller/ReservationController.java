package com.OpenLeaf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.OpenLeaf.domain.ReservationStatus;
import com.OpenLeaf.exception.BookException;
import com.OpenLeaf.exception.ReservationException;
import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.payload.dto.ReservationDTO;
import com.OpenLeaf.payload.request.ReservationRequest;
import com.OpenLeaf.payload.request.ReservationSearchRequest;
import com.OpenLeaf.payload.response.ApiResponse;
import com.OpenLeaf.payload.response.PageResponse;
import com.OpenLeaf.service.ReservationService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ==================== RESERVATION OPERATIONS ====================

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        try {
            ReservationDTO reservation = reservationService.createReservation(reservationRequest);
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch (ReservationException | BookException | UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(e.getMessage(), false)
            );
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createReservationForUser(
            @PathVariable Long userId,
            @Valid @RequestBody ReservationRequest reservationRequest) {
        try {
            ReservationDTO reservation = reservationService.createReservationForUser(userId, reservationRequest);
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch (ReservationException | BookException | UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(e.getMessage(), false)
            );
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        try {
            ReservationDTO reservation = reservationService.cancelReservation(id);
            return ResponseEntity.ok(reservation);
        } catch (ReservationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(e.getMessage(), false)
            );
        }
    }

    @PostMapping("/{id}/fulfill")
    public ResponseEntity<?> fulfillReservation(@PathVariable Long id)
            throws BookException, UserException {
        try {
            ReservationDTO reservation = reservationService
            .fulfillReservation(id);
            return ResponseEntity.ok(reservation);
        } catch (ReservationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(e.getMessage(), false)
            );
        }
    }

    // ==================== QUERY OPERATIONS (UNIFIED SEARCH) ====================

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        try {
            ReservationDTO reservation = reservationService.getReservationById(id);
            return ResponseEntity.ok(reservation);
        } catch (ReservationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(e.getMessage(), false)
            );
        }
    }

    @GetMapping("/my")
    public ResponseEntity<PageResponse<ReservationDTO>> getMyReservations(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "reservedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        ReservationSearchRequest searchRequest = new ReservationSearchRequest();
        searchRequest.setStatus(status);
        searchRequest.setActiveOnly(activeOnly);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);

        PageResponse<ReservationDTO> reservations = reservationService.getMyReservations(searchRequest);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/my/search")
    public ResponseEntity<PageResponse<ReservationDTO>> searchMyReservations(
            @RequestBody ReservationSearchRequest searchRequest) {
        PageResponse<ReservationDTO> reservations = reservationService.getMyReservations(searchRequest);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReservationDTO>> searchReservations(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "reservedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        ReservationSearchRequest searchRequest = new ReservationSearchRequest();
        searchRequest.setUserId(userId);
        searchRequest.setBookId(bookId);
        searchRequest.setStatus(status);
        searchRequest.setActiveOnly(activeOnly);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);

        PageResponse<ReservationDTO> reservations = reservationService.searchReservations(searchRequest);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<ReservationDTO>> advancedSearchReservations(
            @RequestBody ReservationSearchRequest searchRequest) {
        PageResponse<ReservationDTO> reservations = reservationService.searchReservations(searchRequest);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}/queue-position")
    public ResponseEntity<?> getQueuePosition(@PathVariable Long id) {
        try {
            int position = reservationService.getQueuePosition(id);
            return ResponseEntity.ok(new QueuePositionResponse(position));
        } catch (ReservationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(e.getMessage(), false)
            );
        }
    }

    // ==================== RESPONSE DTOs ====================

    public static class QueuePositionResponse {
        public int queuePosition;
        public String message;

        public QueuePositionResponse(int queuePosition) {
            this.queuePosition = queuePosition;
            if (queuePosition == 0) {
                this.message = "Reservation is not in queue";
            } else if (queuePosition == 1) {
                this.message = "You are next in line!";
            } else {
                this.message = "There are " + (queuePosition - 1) + " person(s) ahead of you";
            }
        }
    }
}
