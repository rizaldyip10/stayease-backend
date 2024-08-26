package com.finalproject.stayease.payment.service;

import com.finalproject.stayease.bookings.entity.Booking;
import com.finalproject.stayease.payment.entity.Payment;

public interface PaymentService {
    Payment createPayment(Double amount, String paymentMethod, Booking booking, String paymentStatus);
    Payment uploadPaymentProof(String imageUrl, Long bookingId);
}
