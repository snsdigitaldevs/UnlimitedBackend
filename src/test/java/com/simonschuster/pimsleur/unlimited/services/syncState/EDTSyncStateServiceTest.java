package com.simonschuster.pimsleur.unlimited.services.syncState;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.utils.UnlimitedProgressConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static com.simonschuster.pimsleur.unlimited.utils.PCMProgressConverter.pcmProgressToDto;
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
                List<ProgressDTO> pcmProgressDTOS = pcmProgressToDto(syncStates.getPcmSyncState().getResultData().getUserAppStateData());
                pcmProgressDTOS.forEach(dto -> assertThat(dto.getProductCode().length(), is(13)));

                assertThat(UnlimitedProgressConverter.UnlimitedSyncStateToDTO(syncStates.getUnlimitedSyncState().getResultData().getUserAppStateData())
                        .get(1).getCurrent(), is(true));
                assertThat(syncStates.getUnlimitedSyncState().getResultData()
                        .getUserAppStateData().size(), is(15));
                assertThat(syncStates.getPcmSyncState().getResultData()
                        .getUserAppStateData().size(), is(42));
            }
        });
    }
}