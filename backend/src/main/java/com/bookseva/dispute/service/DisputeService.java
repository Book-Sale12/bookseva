package com.bookseva.dispute.service;

import com.bookseva.dispute.Dispute;
import com.bookseva.dispute.dto.DisputeCreateRequest;
import com.bookseva.dispute.dto.DisputeResolveRequest;
import com.bookseva.dispute.repository.DisputeRepository;
import com.bookseva.notification.service.MailService;
import com.bookseva.notification.service.NotificationService;
import com.bookseva.order.Order;
import com.bookseva.order.OrderStatus;
import com.bookseva.order.repository.OrderRepository;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisputeService {

    private final DisputeRepository disputeRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final MailService mailService;

    @Transactional
    public Dispute raiseDispute(Long orderId, DisputeCreateRequest request, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getBuyer().getEmail().equals(userEmail)) {
            throw new RuntimeException("Only buyer can raise a dispute");
        }

        if (order.getStatus() != OrderStatus.HANDED_OVER) {
            throw new RuntimeException("Dispute can only be raised when order is in HANDED_OVER status");
        }

        order.setStatus(OrderStatus.DISPUTED);
        orderRepository.save(order);

        Dispute dispute = Dispute.builder()
                .order(order)
                .raisedBy(order.getBuyer())
                .reason(request.getReason())
                .description(request.getDescription())
                .evidenceUrls(request.getEvidenceUrls())
                .status(Dispute.DisputeStatus.OPEN)
                .build();

        Dispute savedDispute = disputeRepository.save(dispute);
        
        // Notify seller
        String sellerMsg = "A dispute has been raised against your order for book '" + order.getBook().getTitle() + "'. The order is under review.";
        notificationService.sendNotification(order.getSeller(), "DISPUTE", "Dispute Raised Against Your Order", sellerMsg, null);
        mailService.sendEmail(order.getSeller().getEmail(), "Dispute Raised Against Your Order", sellerMsg);
        
        // Notify buyer
        String buyerMsg = "Your dispute for '" + order.getBook().getTitle() + "' has been received and is under review.";
        notificationService.sendNotification(order.getBuyer(), "DISPUTE", "Dispute Received", buyerMsg, null);
        mailService.sendEmail(order.getBuyer().getEmail(), "Dispute Received", buyerMsg);

        return savedDispute;
    }

    @Transactional(readOnly = true)
    public List<Dispute> getMyDisputes(String userEmail) {
        return disputeRepository.findByInvolvedUserEmail(userEmail);
    }

    @Transactional(readOnly = true)
    public List<Dispute> getAllDisputes() {
        return disputeRepository.findAll();
    }

    @Transactional
    public Dispute resolveDispute(Long disputeId, DisputeResolveRequest request, String adminEmail) {
        Dispute dispute = disputeRepository.findById(disputeId)
                .orElseThrow(() -> new RuntimeException("Dispute not found"));

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        dispute.setStatus(request.getNewStatus());
        dispute.setResolvedBy(admin);
        dispute.setResolvedAt(LocalDateTime.now());
        dispute.setResolutionNotes(request.getResolutionNotes());

        Order order = dispute.getOrder();
        if (request.getNewStatus() == Dispute.DisputeStatus.RESOLVED_DISMISSED) {
            order.setStatus(OrderStatus.COMPLETED); // Proceeds normally
            
            String resolutionNote = request.getResolutionNotes() != null && !request.getResolutionNotes().isBlank() 
                    ? " Note: " + request.getResolutionNotes() : "";
            
            String buyerMsg = "Your dispute has been reviewed and closed. No issue was found with this order." + resolutionNote;
            notificationService.sendNotification(order.getBuyer(), "DISPUTE", "Dispute Closed", buyerMsg, null);
            mailService.sendEmail(order.getBuyer().getEmail(), "Dispute Closed", buyerMsg);
            
            String sellerMsg = "The dispute against your order was reviewed and dismissed. No fault found.";
            notificationService.sendNotification(order.getSeller(), "DISPUTE", "Dispute Dismissed", sellerMsg, null);
            mailService.sendEmail(order.getSeller().getEmail(), "Dispute Dismissed", sellerMsg);
        } else {
            order.setStatus(OrderStatus.REFUNDED); // For partial or full refund
            
            // Deduct trust score from seller
            User seller = order.getSeller();
            int deduction = (request.getNewStatus() == Dispute.DisputeStatus.RESOLVED_REFUND) ? 10 : 5;
            int newScore = Math.max(0, seller.getTrustScore() - deduction);
            seller.setTrustScore(newScore);
            
            // Auto-suspend if score drops below threshold
            if (seller.getTrustScore() < 50) {
                seller.setStatus(com.bookseva.user.UserStatus.SUSPENDED);
            }
            userRepository.save(seller);
            
            String buyerMsg = "Your dispute has been resolved: a full/partial refund has been issued.";
            notificationService.sendNotification(order.getBuyer(), "DISPUTE", "Dispute Resolved", buyerMsg, null);
            mailService.sendEmail(order.getBuyer().getEmail(), "Dispute Resolved", buyerMsg);
            
            String sellerMsg = "Dispute resolved against you. A refund was issued and your trust score was penalized by " + deduction + " points.";
            notificationService.sendNotification(seller, "DISPUTE", "Dispute Resolution Penalty", sellerMsg, null);
            mailService.sendEmail(seller.getEmail(), "Dispute Resolution Penalty", sellerMsg);
        }
        orderRepository.save(order);

        return disputeRepository.save(dispute);
    }
}
