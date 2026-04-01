package com.littletrips.api.service.db;

import com.littletrips.api.model.dto.Transaction;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionLoader {
    private final LocalDBLoader localDBLoader;

    @Autowired
    public TransactionLoader(LocalDBLoader localDBLoader) {
        this.localDBLoader = localDBLoader;
    }

    @Getter
    private List<Transaction> transactions;

    @PostConstruct
    public void loadTransactions() {
        transactions = localDBLoader.load("json/transactions.json", Transaction.class);
    }
}