package com.simonschuster.pimsleur.unlimited.services.customer;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.AggregatedCustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.services.syncState.EDTSyncStateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Moco.eq;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test-config.properties")
public class EDTCustomerInfoServiceTest {

    @MockBean
    private EDTSyncStateService syncStateService;

    @Autowired
    private EDTCustomerInfoService edtCustomerInfoService;

    @Test
    public void shouldGetUnlimitedAndPCMCustomerInfos() throws Exception {

        when(syncStateService.getSyncStates(anyInt(), anyString()))
                .thenReturn(new AggregatedSyncState(null, null));

        HttpServer server = httpServer(12306);
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rsovkolfqxrjl.php")),
                eq(form("action"), "pu_blmqide")))
                .response(file("src/test/resources/unlimitedCustInfoResponse.json"));
        server.post(and(
                by(uri("/subscr_production_v_9/action_handlers/rsovkolfqxrjl.php")),
                eq(form("action"), "pcm_blmqide")))
                .response(file("src/test/resources/pcmCustInfoResponse.json"));

        running(server, new Runnable() {
            @Override
            public void run() {
                AggregatedCustomerInfo customerInfos =
                        edtCustomerInfoService.getCustomerInfos("whatever");
                assertThat(customerInfos.getPcmCustomerInfo().getResultCode(), is(1));
                assertThat(customerInfos.getUnlimitedCustomerInfo().getResultCode(), is(1));
                assertThat(customerInfos.getPcmSyncState(), nullValue());
                assertThat(customerInfos.getUnlimitedSyncState(), nullValue());
            }
        });
    }
}
