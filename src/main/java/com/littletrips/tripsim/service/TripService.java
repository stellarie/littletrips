package com.littletrips.tripsim.service;

import com.littletrips.tripsim.enums.Status;
import com.littletrips.tripsim.enums.TapType;
import com.littletrips.tripsim.model.dto.Transaction;
import com.littletrips.tripsim.model.dto.Trip;
import com.littletrips.tripsim.service.db.TransactionLoader;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TripService {
    private final TransactionLoader transactionLoader;
    private final FareService fareService;

    public TripService(TransactionLoader transactionLoader, FareService fareService) {
        this.transactionLoader = transactionLoader;
        this.fareService = fareService;
    }

    public List<Trip> getTripHistoryByPAN(String pan) {
        List<Transaction> userTransactions = transactionLoader.getTransactions().stream().filter(txn -> txn.pan().equals(pan)).toList();

        // construct trips object one-by-one
        List<Trip> userTrips = new ArrayList<>();
        userTransactions.forEach(txn -> {
            Transaction matchingTxn = findMatchingTransaction(txn, userTransactions);
            if (txn.tapType().equals(TapType.OFF)) {
                return;
            }
            if (matchingTxn != null) {
                userTrips.add(new Trip(
                        txn.dateTimeUTC(),
                        matchingTxn.dateTimeUTC(),
                        ChronoUnit.SECONDS.between(txn.dateTimeUTC(), matchingTxn.dateTimeUTC()),
                        txn.stopId(),
                        matchingTxn.stopId(),
                        fareService.getCostFromStop(txn.stopId(), matchingTxn.stopId()),
                        txn.companyId(),
                        txn.busId(),
                        txn.pan(),
                        txn.stopId().equals(matchingTxn.stopId()) ? Status.CANCELLED : Status.COMPLETED
                ));
            } else {
                userTrips.add(new Trip(
                        txn.dateTimeUTC(),
                        null,
                        null,
                        txn.stopId(),
                        null,
                        fareService.getMaxCostFromStop(txn.stopId()),
                        txn.companyId(),
                        txn.busId(),
                        txn.pan(),
                        Status.INCOMPLETE
                ));
            }
        });
        return userTrips;
    }

    private Transaction findMatchingTransaction (Transaction initialTransaction, List<Transaction> transactions) {
        if (initialTransaction.tapType().equals(TapType.OFF)) {
            return null;
        }

        // sort transactions by date and time first
        List<Transaction> sortedTransactions = transactions
                .stream()
                .sorted(Comparator.comparing(Transaction::dateTimeUTC))
                .toList();

        return sortedTransactions
                .stream()
                .filter(
                        txn -> initialTransaction.busId().equals(txn.busId()) && initialTransaction.companyId().equals(txn.companyId()) && txn.tapType().equals(TapType.OFF)
                ).findFirst()
                .orElse(null);
    }
}
