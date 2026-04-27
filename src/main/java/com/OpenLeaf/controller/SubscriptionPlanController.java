package com.OpenLeaf.controller;

import com.OpenLeaf.exception.SubscriptionPlanException;
import com.OpenLeaf.modal.SubscriptionPlan;
import com.OpenLeaf.payload.dto.SubscriptionPlanDTO;
import com.OpenLeaf.payload.response.ApiResponse;
import com.OpenLeaf.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/subscription-plans")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    // ================ PUBLIC ENDPOINTS ================


    @GetMapping("/active")
    public ResponseEntity<?> getAllActivePlans() {
        try {
            List<SubscriptionPlanDTO> plans = planService.getAllActivePlans();
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            log.error("Failed to fetch active plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to fetch plans: " + e.getMessage(), false));
        }
    }


    @GetMapping("/active/paginated")
    public ResponseEntity<?> getAllActivePlansPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SubscriptionPlanDTO> plans = planService.getAllActivePlans(pageable);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            log.error("Failed to fetch active plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to fetch plans: " + e.getMessage(), false));
        }
    }

    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedPlans() {
        try {
            List<SubscriptionPlanDTO> plans = planService.getFeaturedPlans();
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            log.error("Failed to fetch featured plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to fetch featured plans: " + e.getMessage(), false));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlanById(@PathVariable Long id) {
        try {
            SubscriptionPlanDTO plan = planService.getPlanById(id);
            return ResponseEntity.ok(plan);
        } catch (SubscriptionPlanException e) {
            log.error("Plan not found: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @GetMapping("/code/{planCode}")
    public ResponseEntity<?> getPlanByCode(@PathVariable String planCode) {
        try {
            SubscriptionPlan plan = planService.getPlanByCode(planCode);
            return ResponseEntity.ok(plan);
        } catch (SubscriptionPlanException e) {
            log.error("Plan not found with code: {}", planCode, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @GetMapping("/currency/{currency}")
    public ResponseEntity<?> getPlansByCurrency(@PathVariable String currency) {
        try {
            List<SubscriptionPlanDTO> plans = planService.getPlansByCurrency(currency);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            log.error("Failed to fetch plans by currency", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to fetch plans: " + e.getMessage(), false));
        }
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchPlans(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SubscriptionPlanDTO> plans = planService.searchPlans(q, pageable);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            log.error("Failed to search plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to search plans: " + e.getMessage(), false));
        }
    }

    // ================ ADMIN ENDPOINTS ================

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createPlan(@Valid @RequestBody SubscriptionPlanDTO planDTO) {
        try {
            log.info("Creating new subscription plan: {}", planDTO.getPlanCode());
            SubscriptionPlanDTO createdPlan = planService.createPlan(planDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
        } catch (SubscriptionPlanException e) {
            log.error("Failed to create plan", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionPlanDTO planDTO) {
        try {
            log.info("Updating subscription plan: {}", id);
            SubscriptionPlanDTO updatedPlan = planService.updatePlan(id, planDTO);
            return ResponseEntity.ok(updatedPlan);
        } catch (SubscriptionPlanException e) {
            log.error("Failed to update plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {
        try {
            log.info("Deleting subscription plan: {}", id);
            planService.deletePlan(id);
            return ResponseEntity.ok(new ApiResponse("Subscription plan deactivated successfully", true));
        } catch (SubscriptionPlanException e) {
            log.error("Failed to delete plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/admin/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activatePlan(@PathVariable Long id) {
        try {
            log.info("Activating subscription plan: {}", id);
            SubscriptionPlanDTO plan = planService.activatePlan(id);
            return ResponseEntity.ok(plan);
        } catch (SubscriptionPlanException e) {
            log.error("Failed to activate plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/admin/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivatePlan(@PathVariable Long id) {
        try {
            log.info("Deactivating subscription plan: {}", id);
            SubscriptionPlanDTO plan = planService.deactivatePlan(id);
            return ResponseEntity.ok(plan);
        } catch (SubscriptionPlanException e) {
            log.error("Failed to deactivate plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SubscriptionPlanDTO> plans = planService.getAllPlans(pageable);
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            log.error("Failed to fetch all plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Failed to fetch plans: " + e.getMessage(), false));
        }
    }

    @GetMapping("/admin/check-code")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> checkPlanCode(@RequestParam String code) {
        boolean exists = planService.planCodeExists(code);
        return ResponseEntity.ok(new ApiResponse(
            exists ? "Plan code already exists" : "Plan code is available",
            !exists
        ));
    }
}
