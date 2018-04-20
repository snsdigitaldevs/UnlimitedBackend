package com.simonschuster.pimsleur.unlimited.services.syncState;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class EDTSyncStateServiceTest {

    @Autowired
    private EDTSyncStateService edtSyncStateService;

    @Test
    public void getSyncStates() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/uxpzs.php")),
                eq(form("store_domain"), "ss_pu")))
                .response(file("src/test/resources/unlimitedSyncStateResponse.json"));
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/uxpzs.php")),
                eq(form("store_domain"), "")))
                .response(file("src/test/resources/pcmSyncStateResponse.json"));

        running(server, new Runnable() {
            @Override
            public void run() throws IOException {
                AggregatedSyncState syncStates = edtSyncStateService
                        .getSyncStates(999, "whatever");
                assertThat(syncStates.getUnlimitedSyncState().getResultData()
                        .getUserAppStateData().size(), is(6));
                assertThat(syncStates.getPcmSyncState().getResultData()
                        .getUserAppStateData().size(), is(42));
            }
        });
    }
}