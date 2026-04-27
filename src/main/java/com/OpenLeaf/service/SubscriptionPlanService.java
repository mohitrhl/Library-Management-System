package com.OpenLeaf.service;

import com.OpenLeaf.exception.SubscriptionPlanException;
import com.OpenLeaf.modal.SubscriptionPlan;
import com.OpenLeaf.payload.dto.SubscriptionPlanDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SubscriptionPlanService {


    SubscriptionPlanDTO createPlan(SubscriptionPlanDTO planDTO) throws SubscriptionPlanException;

    SubscriptionPlanDTO updatePlan(Long planId, SubscriptionPlanDTO planDTO) throws SubscriptionPlanException;

    void deletePlan(Long planId) throws SubscriptionPlanException;

    SubscriptionPlanDTO activatePlan(Long planId) throws SubscriptionPlanException;

    SubscriptionPlanDTO deactivatePlan(Long planId) throws SubscriptionPlanException;

    SubscriptionPlanDTO getPlanById(Long planId) throws SubscriptionPlanException;


    SubscriptionPlan getPlanByCode(String planCode) throws SubscriptionPlanException;

    List<SubscriptionPlanDTO> getAllActivePlans();


    Page<SubscriptionPlanDTO> getAllPlans(Pageable pageable);

    Page<SubscriptionPlanDTO> getAllActivePlans(Pageable pageable);

    List<SubscriptionPlanDTO> getFeaturedPlans();

    Page<SubscriptionPlanDTO> searchPlans(String searchTerm, Pageable pageable);

    List<SubscriptionPlanDTO> getPlansByCurrency(String currency);

    boolean planCodeExists(String planCode);
}
