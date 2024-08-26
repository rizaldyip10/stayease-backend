package com.finalproject.stayease.transactions.service;

import com.finalproject.stayease.transactions.dto.TransactionReqDto;

public interface TransactionService {
    void createTransaction(TransactionReqDto reqDto, Long userId);
}
