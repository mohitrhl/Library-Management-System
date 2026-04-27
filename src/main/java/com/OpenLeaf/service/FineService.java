package com.OpenLeaf.service;

import com.OpenLeaf.domain.FineStatus;
import com.OpenLeaf.domain.FineType;
import com.OpenLeaf.exception.FineException;
import com.OpenLeaf.exception.BookLoanException;
import com.OpenLeaf.exception.PaymentException;
import com.OpenLeaf.payload.dto.FineDTO;
import com.OpenLeaf.payload.request.CreateFineRequest;
import com.OpenLeaf.payload.request.WaiveFineRequest;
import com.OpenLeaf.payload.response.PageResponse;
import com.OpenLeaf.payload.response.PaymentInitiateResponse;


public interface FineService {

    // ==================== CREATE OPERATIONS ====================

    FineDTO createFine(CreateFineRequest createRequest) throws BookLoanException;

    PaymentInitiateResponse payFineFully(Long fineId, String transactionId) throws FineException, PaymentException;

    void markFineAsPaid(Long fineId, Long amount, String transactionId);

    // ==================== WAIVER OPERATIONS ====================

    FineDTO waiveFine(WaiveFineRequest waiveRequest) throws FineException;

    // ==================== QUERY OPERATIONS ====================

    FineDTO getFineById(Long fineId) throws FineException;

    java.util.List<FineDTO> getFinesByBookLoanId(Long bookLoanId);

    java.util.List<FineDTO> getMyFines(
            FineStatus status,
            FineType type
    );

    PageResponse<FineDTO> getAllFines(
            FineStatus status,
            FineType type,
            Long userId,
            int page,
            int size
    );

    // ==================== AGGREGATION OPERATIONS ====================

    Long getMyTotalUnpaidFines();

    Long getTotalUnpaidFinesByUserId(Long userId);

    Long getTotalCollectedFines();

    Long getTotalOutstandingFines();

    // ==================== VALIDATION OPERATIONS ====================

    boolean hasUnpaidFines(Long userId);

    void deleteFine(Long fineId) throws FineException;
}
