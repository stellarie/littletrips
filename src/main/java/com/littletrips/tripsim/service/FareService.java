package com.littletrips.tripsim.service;

import com.littletrips.tripsim.model.dto.Destination;
import com.littletrips.tripsim.model.dto.FareTable;
import com.littletrips.tripsim.service.db.FareTableLoader;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class FareService {
    private final FareTableLoader fareTableLoader;
    public FareService(FareTableLoader fareTableLoader) {
        this.fareTableLoader = fareTableLoader;
    }

    public List<FareTable> getFareTable() {
        return fareTableLoader.getFareTable();
    }

    public double getCostFromStop (String fromStopId, String toStopId) {
        List<FareTable> fareTable = fareTableLoader.getFareTable();

        FareTable fareTableDTO = fareTable.stream().filter(tbl -> tbl.stopId().equals(fromStopId)).findFirst().orElse(null);
        if (fareTableDTO != null) {
            Destination toStop = fareTableDTO.destinations().stream().filter(dst -> dst.stopId().equals(toStopId)).findFirst().orElse(null);
            if (toStop != null) {
                return toStop.cost();
            }
        }

        return 0;
    }

    public double getMaxCostFromStop (String fromStopId) {
        List<FareTable> fareTable = fareTableLoader.getFareTable();

        FareTable fareTableDTO = fareTable.stream().filter(tbl -> tbl.stopId().equals(fromStopId)).findFirst().orElse(null);
        if (fareTableDTO != null) {
            Destination maxDestination = fareTableDTO.destinations().stream().max(Comparator.comparing(Destination::cost)).orElse(null);
            if (maxDestination != null) {
                return maxDestination.cost();
            }
        }

        return 0;
    }
}
