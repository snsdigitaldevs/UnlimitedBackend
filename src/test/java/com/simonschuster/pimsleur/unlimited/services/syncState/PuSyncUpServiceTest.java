package com.simonschuster.pimsleur.unlimited.services.syncState;

import com.github.dreamhead.moco.HttpServer;
import com.simonschuster.pimsleur.unlimited.data.dto.syncUp.SyncUpDto;
import com.simonschuster.pimsleur.unlimited.data.dto.syncUp.SyncUpProgressDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class PuSyncUpServiceTest {
    @Autowired
    private SyncUpService puSyncUpService;

    @Test
    public void syncUpPUProgress() throws Exception {
        HttpServer server = httpServer(12306);
        server.post(by(uri("/subscr_production_v_9/action_handlers/uxpzs.php")))
                .response(file("src/test/resources/puSyncUpResponse.json"));

        running(server, () -> {
            SyncUpProgressDto progress = new SyncUpProgressDto();
            progress.setLastPlayHeadLocation(123L);
            progress.setLastChangeTimestamp(456L);

            SyncUpDto syncUpDto = new SyncUpDto();
            syncUpDto.setProgress(progress);

            long lastSaveId = puSyncUpService
                    .syncUpPUProgress("1", "2", "3", "4", syncUpDto);

            assertThat(lastSaveId, is(52L));
        });
    }
}