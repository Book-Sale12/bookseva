package com.bookseva.invoice.service;

import com.bookseva.book.service.CloudinaryService;
import com.bookseva.invoice.Invoice;
import com.bookseva.invoice.repository.InvoiceRepository;
import com.bookseva.order.Order;
import com.bookseva.order.repository.OrderRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CloudinaryService cloudinaryService;
    private final OrderRepository orderRepository;

    @Transactional
    public void generateInvoice(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph("BookSeva Invoice", titleFont));
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Date: " + order.getCreatedAt()));
            document.add(new Paragraph("\n"));
            
            document.add(new Paragraph("Buyer: " + order.getBuyer().getName() + " (" + order.getBuyer().getEmail() + ")"));
            document.add(new Paragraph("Seller: " + order.getSeller().getName() + " (" + order.getSeller().getEmail() + ")"));
            document.add(new Paragraph("\n"));
            
            document.add(new Paragraph("Book: " + order.getBook().getTitle()));
            document.add(new Paragraph("Amount: ₹" + order.getAmount()));
            document.add(new Paragraph("Platform Fee: ₹" + order.getPlatformFee()));
            
            document.close();
            
            byte[] pdfBytes = baos.toByteArray();
            String filename = "invoice_" + order.getId() + ".pdf";
            String fileUrl = cloudinaryService.uploadRaw(pdfBytes, filename);
            
            Invoice invoice = Invoice.builder()
                    .order(order)
                    .fileUrl(fileUrl)
                    .build();
            
            invoiceRepository.save(invoice);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice", e);
        }
    }

    @Transactional(readOnly = true)
    public String getInvoiceUrl(Long orderId, String userEmail) {
        Invoice invoice = invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Check if user is buyer or seller
        if (!invoice.getOrder().getBuyer().getEmail().equals(userEmail) && !invoice.getOrder().getSeller().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized to access this invoice");
        }

        return invoice.getFileUrl();
    }
}
