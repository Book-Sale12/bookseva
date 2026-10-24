package com.bookseva.payment.dto;
import com.bookseva.payment.entity.Payment;


public class VerifyPaymentRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    public VerifyPaymentRequest() {
    }

    public String getRazorpayOrderId() {
        return this.razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return this.razorpayPaymentId;
    }

    public String getRazorpaySignature() {
        return this.razorpaySignature;
    }

    public void setRazorpayOrderId(final String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public void setRazorpayPaymentId(final String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public void setRazorpaySignature(final String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof VerifyPaymentRequest)) return false;
        final VerifyPaymentRequest other = (VerifyPaymentRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$razorpayOrderId = this.getRazorpayOrderId();
        final Object other$razorpayOrderId = other.getRazorpayOrderId();
        if (this$razorpayOrderId == null ? other$razorpayOrderId != null : !this$razorpayOrderId.equals(other$razorpayOrderId)) return false;
        final Object this$razorpayPaymentId = this.getRazorpayPaymentId();
        final Object other$razorpayPaymentId = other.getRazorpayPaymentId();
        if (this$razorpayPaymentId == null ? other$razorpayPaymentId != null : !this$razorpayPaymentId.equals(other$razorpayPaymentId)) return false;
        final Object this$razorpaySignature = this.getRazorpaySignature();
        final Object other$razorpaySignature = other.getRazorpaySignature();
        if (this$razorpaySignature == null ? other$razorpaySignature != null : !this$razorpaySignature.equals(other$razorpaySignature)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof VerifyPaymentRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $razorpayOrderId = this.getRazorpayOrderId();
        result = result * PRIME + ($razorpayOrderId == null ? 43 : $razorpayOrderId.hashCode());
        final Object $razorpayPaymentId = this.getRazorpayPaymentId();
        result = result * PRIME + ($razorpayPaymentId == null ? 43 : $razorpayPaymentId.hashCode());
        final Object $razorpaySignature = this.getRazorpaySignature();
        result = result * PRIME + ($razorpaySignature == null ? 43 : $razorpaySignature.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "VerifyPaymentRequest(razorpayOrderId=" + this.getRazorpayOrderId() + ", razorpayPaymentId=" + this.getRazorpayPaymentId() + ", razorpaySignature=" + this.getRazorpaySignature() + ")";
    }
}
