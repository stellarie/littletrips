package com.littletrips.api.service;

import com.littletrips.api.model.dto.Destination;
import com.littletrips.api.model.dto.FareTable;
import com.littletrips.api.service.db.FareTableLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareServiceTest {

    @Mock
    private FareTableLoader fareTableLoader;

    @InjectMocks
    private FareService fareService;

    private List<FareTable> fareTable;

    @BeforeEach
    void setUp() {
        Destination stop1ToStop2 = new Destination("Stop2", 3.25);
        Destination stop1ToStop3 = new Destination("Stop3", 7.30);
        Destination stop2ToStop1 = new Destination("Stop1", 3.25);
        Destination stop2ToStop3 = new Destination("Stop3", 5.50);

        FareTable stop1 = new FareTable("Stop1", List.of(stop1ToStop2, stop1ToStop3));
        FareTable stop2 = new FareTable("Stop2", List.of(stop2ToStop1, stop2ToStop3));
        FareTable stopNoDestinations = new FareTable("StopEmpty", Collections.emptyList());

        fareTable = List.of(stop1, stop2, stopNoDestinations);
    }

    @Test
    void getFareTable_returnsDelegatedList() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        List<FareTable> result = fareService.getFareTable();

        assertThat(result).isEqualTo(fareTable);
        verify(fareTableLoader, times(1)).getFareTable();
    }

    @Test
    void getCostFromStop_returnsCorrectCost() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        double cost = fareService.getCostFromStop("Stop1", "Stop2");

        assertThat(cost).isEqualTo(3.25);
    }

    @Test
    void getCostFromStop_returnsCorrectCostForHigherFare() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        double cost = fareService.getCostFromStop("Stop1", "Stop3");

        assertThat(cost).isEqualTo(7.30);
    }

    @Test
    void getCostFromStop_returnsZeroWhenFromStopNotFound() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        double cost = fareService.getCostFromStop("StopX", "Stop2");

        assertThat(cost).isEqualTo(0);
    }

    @Test
    void getCostFromStop_returnsZeroWhenToStopNotFound() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        double cost = fareService.getCostFromStop("Stop1", "StopX");

        assertThat(cost).isEqualTo(0);
    }

    @Test
    void getCostFromStop_returnsZeroWhenFareTableIsEmpty() {
        when(fareTableLoader.getFareTable()).thenReturn(Collections.emptyList());

        double cost = fareService.getCostFromStop("Stop1", "Stop2");

        assertThat(cost).isEqualTo(0);
    }

    @Test
    void getCostFromStop_returnsZeroWhenFromStopHasNoDestinations() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        double cost = fareService.getCostFromStop("StopEmpty", "Stop1");

        assertThat(cost).isEqualTo(0);
    }

    @Test
    void getMaxCostFromStop_returnsHighestCostDestination() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        double maxCost = fareService.getMaxCostFromStop("Stop1");

        assertThat(maxCost).isEqualTo(7.30);
    }

    @Test
    void getMaxCostFromStop_returnsCorrectMaxForStop2() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        double maxCost = fareService.getMaxCostFromStop("Stop2");

        assertThat(maxCost).isEqualTo(5.50);
    }

    @Test
    void getMaxCostFromStop_returnsZeroWhenFromStopNotFound() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        double maxCost = fareService.getMaxCostFromStop("StopX");

        assertThat(maxCost).isEqualTo(0);
    }

    @Test
    void getMaxCostFromStop_returnsZeroWhenFareTableIsEmpty() {
        when(fareTableLoader.getFareTable()).thenReturn(Collections.emptyList());

        double maxCost = fareService.getMaxCostFromStop("Stop1");

        assertThat(maxCost).isEqualTo(0);
    }

    @Test
    void getMaxCostFromStop_returnsZeroWhenStopHasNoDestinations() {
        when(fareTableLoader.getFareTable()).thenReturn(fareTable);

        double maxCost = fareService.getMaxCostFromStop("StopEmpty");

        assertThat(maxCost).isEqualTo(0);
    }
}