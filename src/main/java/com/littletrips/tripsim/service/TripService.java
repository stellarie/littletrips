package com.littletrips.tripsim.service;

import com.littletrips.tripsim.enums.Status;
import com.littletrips.tripsim.enums.TapType;
import com.littletrips.tripsim.model.dto.Transaction;
import com.littletrips.tripsim.model.dto.Trip;
import com.littletrips.tripsim.model.dto.TripHistory;
import com.littletrips.tripsim.service.db.TransactionLoader;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class TripService {
    private final TransactionLoader transactionLoader;
    private final FareService fareService;

    public TripService(TransactionLoader transactionLoader, FareService fareService) {
        this.transactionLoader = transactionLoader;
        this.fareService = fareService;
    }

    public TripHistory getTripHistoryByPAN(String pan, Integer page, Integer size) {
        List<Transaction> userTransactions = transactionLoader.getTransactions().stream()
                .filter(txn -> txn.pan().equals(pan))
                .sorted(Comparator.comparing(Transaction::dateTimeUTC))
                .toList();

        List<Trip> userTrips = new ArrayList<>();

        userTransactions.forEach(txn -> {
            if (txn.tapType().equals(TapType.OFF)) {
                return;
            }

            Transaction matchingTxn = findMatchingTransaction(txn, userTransactions);
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

        userTrips.sort(Comparator.comparing(Trip::startTime).reversed());

        if (Objects.isNull(size)) {
            return new TripHistory(userTrips, userTrips.size(), 1, userTrips.size(),
                    getMonthSpending(userTrips), getLifetimeSpending(userTrips), getMonthTripCount(userTrips));
        }

        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, userTrips.size());

        if (startIndex >= userTrips.size() || startIndex < 0) {
            return new TripHistory(new ArrayList<>(), userTrips.size(), page, size, 0, 0, 0);
        }

        List<Trip> tripSubList = userTrips.subList(startIndex, endIndex);
        return new TripHistory(tripSubList, userTrips.size(), page, size,
                getMonthSpending(userTrips), getLifetimeSpending(userTrips), getMonthTripCount(userTrips));
    }

    private Transaction findMatchingTransaction(Transaction initialTransaction, List<Transaction> transactions) {
        if (initialTransaction.tapType() != TapType.ON) {
            return null;
        }

        return transactions.stream()
                .filter(txn -> txn.pan().equals(initialTransaction.pan()))
                .filter(txn -> txn.tapType() == TapType.OFF)
                .filter(txn -> txn.busId().equals(initialTransaction.busId()))
                .filter(txn -> txn.companyId().equals(initialTransaction.companyId()))
                .filter(txn -> txn.dateTimeUTC().isAfter(initialTransaction.dateTimeUTC()))
                .min(Comparator.comparing(Transaction::dateTimeUTC))
                .orElse(null);
    }

    private int getMonthTripCount (List<Trip> trips) {
        return trips.stream()
                .filter(t -> t.startTime().getMonth() == LocalDateTime.now().getMonth() && t.startTime().getYear() == LocalDateTime.now().getYear())
                .toList()
                .size();
    }
    private double getMonthSpending (List<Trip> trips) {
        List<Trip> monthTrips = trips.stream()
                .filter(t -> t.startTime().getMonth() == LocalDateTime.now().getMonth() && t.startTime().getYear() == LocalDateTime.now().getYear())
                .toList();
        return monthTrips.stream()
                .map(Trip::chargeAmount)
                .reduce(0.0, Double::sum);
    }

    private double getLifetimeSpending (List<Trip> trips) {
        return trips.stream()
                .map(Trip::chargeAmount)
                .reduce(0.0, Double::sum);
    }
}
