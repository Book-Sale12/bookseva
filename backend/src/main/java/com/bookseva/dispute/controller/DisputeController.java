package com.bookseva.dispute.controller;
import com.bookseva.dispute.entity.Dispute;


import com.bookseva.dispute.dto.DisputeCreateRequest;
import com.bookseva.dispute.service.DisputeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DisputeController {
    private final DisputeService disputeService;

    @PostMapping("/orders/{orderId}/disputes")
    public ResponseEntity<?> raiseDispute(@PathVariable Long orderId, @Valid @RequestBody DisputeCreateRequest request, Authentication authentication) {
        var dispute = disputeService.raiseDispute(orderId, request, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", dispute));
    }

    @GetMapping("/disputes/mine")
    public ResponseEntity<?> getMyDisputes(Authentication authentication) {
        var disputes = disputeService.getMyDisputes(authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", disputes));
    }

    public DisputeController(final DisputeService disputeService) {
        this.disputeService = disputeService;
    }
}
