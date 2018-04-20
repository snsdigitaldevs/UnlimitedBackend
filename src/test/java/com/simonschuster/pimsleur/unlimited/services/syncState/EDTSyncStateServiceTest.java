package com.simonschuster.pimsleur.unlimited.services.syncState;

import com.simonschuster.pimsleur.unlimited.data.domain.syncState.AggregatedSyncState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EDTSyncStateServiceTest {

    @Autowired
    private EDTSyncStateService edtSyncStateService;

    @Test
    public void getSyncStates() {
        AggregatedSyncState syncStates = edtSyncStateService.getSyncStates(124302, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwaW1zbGV1cmRpZ2l0YWwuY29tIiwiYXVkIjoiY291cnNlbWFuYWdlciIsImlhdCI6MTUyNDAyOTAyOCwibmJmIjoxNTI0MDI5MDI4LCJjb250ZXh0Ijp7ImN1c3RvbWVyc0lkIjoxMjQzMDJ9fQ.d-JPkQtC3w3e1pU5Vm0pQv8hVV4egyi7Kco_4v5ro4I");

    }
}