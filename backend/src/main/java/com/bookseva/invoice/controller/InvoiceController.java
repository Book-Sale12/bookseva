package com.bookseva.invoice.controller;

import com.bookseva.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getInvoiceUrl(@PathVariable Long orderId, Authentication authentication) {
        String url = invoiceService.getInvoiceUrl(orderId, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", Map.of("url", url)));
    }
}
