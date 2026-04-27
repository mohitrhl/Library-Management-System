package com.OpenLeaf.service;

import com.OpenLeaf.exception.BookException;
import com.OpenLeaf.exception.ReservationException;
import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.payload.dto.ReservationDTO;
import com.OpenLeaf.payload.request.ReservationRequest;
import com.OpenLeaf.payload.request.ReservationSearchRequest;
import com.OpenLeaf.payload.response.PageResponse;

public interface ReservationService {

    // ==================== RESERVATION OPERATIONS ====================

    ReservationDTO createReservation(ReservationRequest reservationRequest)
        throws ReservationException, BookException, UserException;

    ReservationDTO createReservationForUser(Long userId,
                                            ReservationRequest reservationRequest)
            throws ReservationException, BookException, UserException;

    ReservationDTO cancelReservation(Long reservationId) throws ReservationException;

    ReservationDTO fulfillReservation(Long reservationId) throws ReservationException, BookException, UserException;

    // ==================== QUERY OPERATIONS ====================

    ReservationDTO getReservationById(Long reservationId) throws ReservationException;

    PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest searchRequest);

    PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest searchRequest);

    int getQueuePosition(Long reservationId) throws ReservationException;

    // ==================== ADMIN OPERATIONS ====================

    void processNextReservation(Long bookId);

    int expireOldReservations();

    void updateQueuePositions(Long bookId);
}
