package com.littletrips.tripsim.service;

import com.littletrips.tripsim.enums.Status;
import com.littletrips.tripsim.enums.TapType;
import com.littletrips.tripsim.model.dto.Transaction;
import com.littletrips.tripsim.model.dto.Trip;
import com.littletrips.tripsim.model.dto.TripHistory;
import com.littletrips.tripsim.service.db.TransactionLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TransactionLoader transactionLoader;

    @Mock
    private FareService fareService;

    @InjectMocks
    private TripService tripService;

    private static final String PAN = "5500005555555559";
    private static final String OTHER_PAN = "4111111111111111";
    private static final String COMPANY = "Company1";
    private static final String BUS = "Bus1";

    private final LocalDateTime baseTime = LocalDateTime.of(2025, 3, 15, 9, 0, 0);
    private final AtomicInteger idSeq = new AtomicInteger(1);

    private Transaction tapOn(String pan, String stopId, String busId, LocalDateTime time) {
        return new Transaction(idSeq.getAndIncrement(), time, TapType.ON, stopId, COMPANY, busId, pan);
    }

    private Transaction tapOff(String pan, String stopId, String busId, LocalDateTime time) {
        return new Transaction(idSeq.getAndIncrement(), time, TapType.OFF, stopId, COMPANY, busId, pan);
    }

    @Test
    void getTripHistoryByPAN_completedTrip_allFieldsPopulated() {
        Transaction on = tapOn(PAN, "Stop1", BUS, baseTime);
        Transaction off = tapOff(PAN, "Stop2", BUS, baseTime.plusMinutes(10));

        when(transactionLoader.getTransactions()).thenReturn(List.of(on, off));
        when(fareService.getCostFromStop("Stop1", "Stop2")).thenReturn(3.25);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips()).hasSize(1);
        Trip trip = history.trips().get(0);
        assertThat(trip.fromStopId()).isEqualTo("Stop1");
        assertThat(trip.toStopId()).isEqualTo("Stop2");
        assertThat(trip.chargeAmount()).isEqualTo(3.25);
        assertThat(trip.status()).isEqualTo(Status.COMPLETED);
        assertThat(trip.durationSecs()).isEqualTo(600L);
    }

    @Test
    void getTripHistoryByPAN_cancelledTrip_sameFromAndToStop() {
        Transaction on = tapOn(PAN, "Stop1", BUS, baseTime);
        Transaction off = tapOff(PAN, "Stop1", BUS, baseTime.plusMinutes(2));

        when(transactionLoader.getTransactions()).thenReturn(List.of(on, off));
        when(fareService.getCostFromStop("Stop1", "Stop1")).thenReturn(0.0);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips()).hasSize(1);
        assertThat(history.trips().get(0).status()).isEqualTo(Status.CANCELLED);
    }

    @Test
    void getTripHistoryByPAN_incompleteTrip_noMatchingTapOff() {
        Transaction on = tapOn(PAN, "Stop1", BUS, baseTime);

        when(transactionLoader.getTransactions()).thenReturn(List.of(on));
        when(fareService.getMaxCostFromStop("Stop1")).thenReturn(7.30);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips()).hasSize(1);
        Trip trip = history.trips().get(0);
        assertThat(trip.status()).isEqualTo(Status.INCOMPLETE);
        assertThat(trip.toStopId()).isNull();
        assertThat(trip.endTime()).isNull();
        assertThat(trip.durationSecs()).isNull();
        assertThat(trip.chargeAmount()).isEqualTo(7.30);
    }

    @Test
    void getTripHistoryByPAN_standaloneOffTransaction_isIgnored() {
        Transaction off = tapOff(PAN, "Stop2", BUS, baseTime.plusMinutes(10));

        when(transactionLoader.getTransactions()).thenReturn(List.of(off));

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips()).isEmpty();
    }

    @Test
    void getTripHistoryByPAN_filtersOutOtherPANs() {
        Transaction onOther = tapOn(OTHER_PAN, "Stop1", BUS, baseTime);
        Transaction offOther = tapOff(OTHER_PAN, "Stop2", BUS, baseTime.plusMinutes(10));
        Transaction on = tapOn(PAN, "Stop3", "Bus2", baseTime.plusMinutes(5));

        when(transactionLoader.getTransactions()).thenReturn(List.of(onOther, offOther, on));
        when(fareService.getMaxCostFromStop("Stop3")).thenReturn(5.50);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips()).hasSize(1);
        assertThat(history.trips().get(0).pan()).isEqualTo(PAN);
    }

    @Test
    void getTripHistoryByPAN_noTransactionsForPAN_returnsEmptyHistory() {
        when(transactionLoader.getTransactions()).thenReturn(Collections.emptyList());

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips()).isEmpty();
        assertThat(history.total()).isEqualTo(0);
    }

    @Test
    void getTripHistoryByPAN_multipleCompletedTrips_allReturned() {
        Transaction on1 = tapOn(PAN, "Stop1", BUS, baseTime);
        Transaction off1 = tapOff(PAN, "Stop2", BUS, baseTime.plusMinutes(10));
        Transaction on2 = tapOn(PAN, "Stop2", "Bus2", baseTime.plusHours(1));
        Transaction off2 = tapOff(PAN, "Stop3", "Bus2", baseTime.plusHours(1).plusMinutes(15));

        when(transactionLoader.getTransactions()).thenReturn(List.of(on1, off1, on2, off2));
        when(fareService.getCostFromStop("Stop1", "Stop2")).thenReturn(3.25);
        when(fareService.getCostFromStop("Stop2", "Stop3")).thenReturn(5.50);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips()).hasSize(2);
        assertThat(history.total()).isEqualTo(2);
    }

    @Test
    void getTripHistoryByPAN_nullSize_returnsAllTripsOnOnePage() {
        Transaction on1 = tapOn(PAN, "Stop1", BUS, baseTime);
        Transaction off1 = tapOff(PAN, "Stop2", BUS, baseTime.plusMinutes(10));
        Transaction on2 = tapOn(PAN, "Stop2", "Bus2", baseTime.plusHours(1));
        Transaction off2 = tapOff(PAN, "Stop3", "Bus2", baseTime.plusHours(2));

        when(transactionLoader.getTransactions()).thenReturn(List.of(on1, off1, on2, off2));
        when(fareService.getCostFromStop(anyString(), anyString())).thenReturn(3.25);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips()).hasSize(2);
        assertThat(history.page()).isEqualTo(1);
        assertThat(history.pageSize()).isEqualTo(2);
    }

    @Test
    void getTripHistoryByPAN_withPagination_returnsCorrectPage() {
        Transaction on1 = tapOn(PAN, "Stop1", BUS, baseTime);
        Transaction off1 = tapOff(PAN, "Stop2", BUS, baseTime.plusMinutes(10));
        Transaction on2 = tapOn(PAN, "Stop2", "Bus2", baseTime.plusHours(1));
        Transaction off2 = tapOff(PAN, "Stop3", "Bus2", baseTime.plusHours(2));
        Transaction on3 = tapOn(PAN, "Stop3", "Bus3", baseTime.plusHours(3));
        Transaction off3 = tapOff(PAN, "Stop1", "Bus3", baseTime.plusHours(4));

        when(transactionLoader.getTransactions()).thenReturn(List.of(on1, off1, on2, off2, on3, off3));
        when(fareService.getCostFromStop(anyString(), anyString())).thenReturn(3.25);

        TripHistory page1 = tripService.getTripHistoryByPAN(PAN, 1, 2);
        TripHistory page2 = tripService.getTripHistoryByPAN(PAN, 2, 2);

        assertThat(page1.trips()).hasSize(2);
        assertThat(page1.page()).isEqualTo(1);
        assertThat(page2.trips()).hasSize(1);
        assertThat(page2.page()).isEqualTo(2);
        assertThat(page1.total()).isEqualTo(3);
    }

    @Test
    void getTripHistoryByPAN_pageExceedsTotalTrips_returnsEmptyTripList() {
        Transaction on = tapOn(PAN, "Stop1", BUS, baseTime);
        Transaction off = tapOff(PAN, "Stop2", BUS, baseTime.plusMinutes(10));

        when(transactionLoader.getTransactions()).thenReturn(List.of(on, off));
        when(fareService.getCostFromStop(anyString(), anyString())).thenReturn(3.25);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 99, 2);

        assertThat(history.trips()).isEmpty();
        assertThat(history.pageSize()).isEqualTo(0);
    }

    @Test
    void getTripHistoryByPAN_lastPageIsPartial_returnsRemainingTrips() {
        Transaction on1 = tapOn(PAN, "Stop1", BUS, baseTime);
        Transaction off1 = tapOff(PAN, "Stop2", BUS, baseTime.plusMinutes(10));
        Transaction on2 = tapOn(PAN, "Stop2", "Bus2", baseTime.plusHours(1));
        Transaction off2 = tapOff(PAN, "Stop3", "Bus2", baseTime.plusHours(2));
        Transaction on3 = tapOn(PAN, "Stop3", "Bus3", baseTime.plusHours(3));
        Transaction off3 = tapOff(PAN, "Stop1", "Bus3", baseTime.plusHours(4));

        when(transactionLoader.getTransactions()).thenReturn(List.of(on1, off1, on2, off2, on3, off3));
        when(fareService.getCostFromStop(anyString(), anyString())).thenReturn(3.25);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 2, 2);

        assertThat(history.trips()).hasSize(1);
        assertThat(history.pageSize()).isEqualTo(1);
    }

    @Test
    void getTripHistoryByPAN_lifetimeSpending_sumsAllTripCharges() {
        Transaction on1 = tapOn(PAN, "Stop1", BUS, baseTime);
        Transaction off1 = tapOff(PAN, "Stop2", BUS, baseTime.plusMinutes(10));
        Transaction on2 = tapOn(PAN, "Stop2", "Bus2", baseTime.plusHours(1));
        Transaction off2 = tapOff(PAN, "Stop3", "Bus2", baseTime.plusHours(2));

        when(transactionLoader.getTransactions()).thenReturn(List.of(on1, off1, on2, off2));
        when(fareService.getCostFromStop("Stop1", "Stop2")).thenReturn(3.25);
        when(fareService.getCostFromStop("Stop2", "Stop3")).thenReturn(5.50);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.lifetimeSpend()).isEqualTo(8.75);
    }

    @Test
    void getTripHistoryByPAN_monthSpending_onlyIncludesCurrentMonthTrips() {
        LocalDateTime currentMonthTime = LocalDateTime.now().withDayOfMonth(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime lastMonthTime = currentMonthTime.minusMonths(1);

        Transaction onCurrent = tapOn(PAN, "Stop1", BUS, currentMonthTime);
        Transaction offCurrent = tapOff(PAN, "Stop2", BUS, currentMonthTime.plusMinutes(10));
        Transaction onPast = tapOn(PAN, "Stop2", "Bus2", lastMonthTime);
        Transaction offPast = tapOff(PAN, "Stop3", "Bus2", lastMonthTime.plusMinutes(15));

        when(transactionLoader.getTransactions()).thenReturn(List.of(onCurrent, offCurrent, onPast, offPast));
        when(fareService.getCostFromStop("Stop1", "Stop2")).thenReturn(3.25);
        when(fareService.getCostFromStop("Stop2", "Stop3")).thenReturn(5.50);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.monthSpend()).isEqualTo(3.25);
        assertThat(history.lifetimeSpend()).isEqualTo(8.75);
    }

    @Test
    void getTripHistoryByPAN_monthTripCount_onlyCountsCurrentMonthTrips() {
        LocalDateTime currentMonthTime = LocalDateTime.now().withDayOfMonth(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime lastMonthTime = currentMonthTime.minusMonths(1);

        Transaction onCurrent = tapOn(PAN, "Stop1", BUS, currentMonthTime);
        Transaction offCurrent = tapOff(PAN, "Stop2", BUS, currentMonthTime.plusMinutes(10));
        Transaction onPast = tapOn(PAN, "Stop2", "Bus2", lastMonthTime);
        Transaction offPast = tapOff(PAN, "Stop3", "Bus2", lastMonthTime.plusMinutes(15));

        when(transactionLoader.getTransactions()).thenReturn(List.of(onCurrent, offCurrent, onPast, offPast));
        when(fareService.getCostFromStop(anyString(), anyString())).thenReturn(3.25);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.monthTripCount()).isEqualTo(1);
    }

    @Test
    void getTripHistoryByPAN_incompleteTrip_chargesMaxFare() {
        Transaction on = tapOn(PAN, "Stop1", BUS, baseTime);

        when(transactionLoader.getTransactions()).thenReturn(List.of(on));
        when(fareService.getMaxCostFromStop("Stop1")).thenReturn(7.30);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips().get(0).chargeAmount()).isEqualTo(7.30);
        verify(fareService, never()).getCostFromStop(anyString(), anyString());
    }

    @Test
    void getTripHistoryByPAN_tapOffMatchesBusAndCompany() {
        Transaction on = tapOn(PAN, "Stop1", BUS, baseTime);
        Transaction offDifferentBus = tapOff(PAN, "Stop2", "Bus99", baseTime.plusMinutes(10));

        when(transactionLoader.getTransactions()).thenReturn(List.of(on, offDifferentBus));
        when(fareService.getMaxCostFromStop("Stop1")).thenReturn(7.30);

        TripHistory history = tripService.getTripHistoryByPAN(PAN, 1, null);

        assertThat(history.trips().get(0).status()).isEqualTo(Status.INCOMPLETE);
    }
}