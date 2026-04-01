package com.littletrips.api.service.db;

import com.littletrips.api.model.dto.FareTable;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FareTableLoader {
    private final LocalDBLoader localDBLoader;

    @Autowired
    public FareTableLoader(LocalDBLoader localDBLoader) {
        this.localDBLoader = localDBLoader;
    }

    @Getter
    private List<FareTable> fareTable;

    @PostConstruct
    public void loadFareTable() {
        fareTable = localDBLoader.load("json/fare.json", FareTable.class);
    }
}