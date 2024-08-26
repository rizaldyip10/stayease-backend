package com.finalproject.stayease.transactions.service.impl;

import com.finalproject.stayease.bookings.entity.Booking;
import com.finalproject.stayease.bookings.service.BookingService;
import com.finalproject.stayease.midtrans.dto.BankTransfer;
import com.finalproject.stayease.midtrans.dto.MidtransReqDto;
import com.finalproject.stayease.midtrans.dto.TransactionDetail;
import com.finalproject.stayease.payment.entity.Payment;
import com.finalproject.stayease.payment.service.PaymentService;
import com.finalproject.stayease.transactions.midtrans.service.MidtransService;
import com.finalproject.stayease.transactions.dto.TransactionReqDto;
import com.finalproject.stayease.transactions.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final MidtransService midtransService;

    public TransactionServiceImpl(BookingService bookingService, PaymentService paymentService, MidtransService midtransService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.midtransService = midtransService;
    }

    @Override
    public void createTransaction(TransactionReqDto reqDto, Long userId) {
        Booking newBooking = bookingService.createBooking(reqDto.getBooking(), userId);
        MidtransReqDto midtransReqDto = new MidtransReqDto();

        var transactionDetail = new TransactionDetail();
        transactionDetail.setOrder_id(String.valueOf(newBooking.getId()));
        transactionDetail.setGross_amount(reqDto.getAmount());

        var bankTransfer = new BankTransfer();
        bankTransfer.setBank(reqDto.getBank());

        midtransReqDto.setTransaction_details(transactionDetail);
        midtransReqDto.setBank_transfer(bankTransfer);

        midtransReqDto.setPayment_type(reqDto.getPaymentMethod());

        var midtrans = midtransService.createTransaction(midtransReqDto);

        Payment newPayment = paymentService.createPayment(reqDto.getAmount(), reqDto.getPaymentMethod(), newBooking, (String) midtrans.get("transaction_status"));
    }
}
